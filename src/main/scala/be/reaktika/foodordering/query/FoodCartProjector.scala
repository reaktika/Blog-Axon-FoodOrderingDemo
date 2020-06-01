package be.reaktika.foodordering.query

import java.util.UUID

import be.reaktika.foodordering.coreapi.Events.{FoodCartCreatedEvent, ProductDeselectedEvent, ProductSelectedEvent}
import be.reaktika.foodordering.coreapi.Queries.{FindFoodCartQuery, FindFoodCartsQuery}
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler

import scala.collection.mutable

class FoodCartProjector(foodCartViewRepository: FoodCartViewRepository) {

  @EventHandler
  def on(event: FoodCartCreatedEvent): Unit = {
    val foodCartView = FoodCartView(event.foodCartId, mutable.Map.empty[UUID, Int])
    foodCartViewRepository.save(event.foodCartId, foodCartView)
  }

  @EventHandler
  def on(event: ProductSelectedEvent): Unit = {
    foodCartViewRepository.findById(event.foodCartId).foreach(foodCartView => foodCartView.addProducts(event.productId, event.quantity))
  }

  @EventHandler
  def on(event: ProductDeselectedEvent): Unit = {
    foodCartViewRepository.findById(event.foodCartId).foreach(foodCartView => foodCartView.removeProducts(event.productId, event.quantity))
  }

  @QueryHandler
  def handle(query: FindFoodCartQuery): FoodCartViewResult = {
    FoodCartViewResult(foodCartViewRepository.findById(query.foodCartId))
  }

  @QueryHandler
  def handle(query: FindFoodCartsQuery): FoodCartsViewResult = {
    val cartIds = foodCartViewRepository.catalog.keys
    FoodCartsViewResult(cartIds.toList)
  }
}
