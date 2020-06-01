package be.reaktika.foodordering.coreapi

import java.util.UUID

object Events {
  case class FoodCartCreatedEvent(foodCartId: UUID)
  case class ProductSelectedEvent(foodCartId: UUID, productId: UUID, quantity: Int)
  case class ProductDeselectedEvent(foodCartId: UUID, productId: UUID, quantity: Int)
  case class OrderConfirmedEvent(foodCartId: UUID)
}
