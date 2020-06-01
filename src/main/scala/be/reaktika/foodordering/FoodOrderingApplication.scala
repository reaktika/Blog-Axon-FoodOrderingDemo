package be.reaktika.foodordering

import be.reaktika.foodordering.command.FoodCart
import be.reaktika.foodordering.gui.FoodOrderingController
import be.reaktika.foodordering.query.{FoodCartProjector, FoodCartViewRepository}
import org.axonframework.config.DefaultConfigurer
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine

object FoodOrderingApplication extends App {

  val foodCartViewRepository = new FoodCartViewRepository
  val projector = new FoodCartProjector(foodCartViewRepository)

  val configuration = DefaultConfigurer.defaultConfiguration()
    .configureAggregate(classOf[FoodCart])
    //.configureEmbeddedEventStore(_ => new InMemoryEventStorageEngine)
    .eventProcessing(ep => ep.registerEventHandler(_ => projector))
    .registerQueryHandler(_ => projector)
    .buildConfiguration()
  configuration.start()

  new FoodOrderingController(configuration)
}
