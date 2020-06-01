package be.reaktika.foodordering.gui

import java.util.UUID
import java.util.concurrent.CompletableFuture

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import be.reaktika.foodordering.coreapi.Commands.{CreateFoodCartCommand, DeselectProductCommand, SelectProductCommand}
import be.reaktika.foodordering.coreapi.Queries.{FindFoodCartQuery, FindFoodCartsQuery}
import be.reaktika.foodordering.query.{FoodCartViewResult, FoodCartsViewResult}
import org.axonframework.config.Configuration
import org.axonframework.messaging.responsetypes.ResponseTypes

import scala.concurrent.ExecutionContextExecutor

class FoodOrderingController(config: Configuration) {

  implicit val system: ActorSystem = ActorSystem("my-controller-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = concat(
    pathPrefix("foodCart") {
      concat(
        path("create") {
          post {
            val response: CompletableFuture[UUID] = config.commandGateway().send(CreateFoodCartCommand(UUID.randomUUID()))
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"${response.get}"))
          }
        },
        pathPrefix(JavaUUID) { foodCartId =>
          concat(
            pathPrefix("select" / JavaUUID / "quantity" / IntNumber) { (productId, quantity) =>
              post {
                val _ = config.commandGateway().send(new SelectProductCommand(foodCartId, productId, quantity)).get
                complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"Selected"))
              }
            },
            pathPrefix("deselect" / JavaUUID / "quantity" / IntNumber) { (productId, quantity) =>
              post {
                val _ = config.commandGateway().send(DeselectProductCommand(foodCartId, productId, quantity)).get
                complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"Deselected"))
              }
            },
            pathEnd {
              get {
                val response = config.queryGateway()
                  .query(
                    FindFoodCartQuery(foodCartId),
                    ResponseTypes.instanceOf(classOf[FoodCartViewResult])
                  ).get
                complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"$response"))
              }
            }
          )
        }
      )
    },
    path("foodCarts") {
      get {
        val response = config.queryGateway()
          .query(
            FindFoodCartsQuery(),
            ResponseTypes.instanceOf(classOf[FoodCartsViewResult])
          ).get
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"$response"))
      }
    }
  )

  Http().bindAndHandle(route, "localhost", 8080)
}
