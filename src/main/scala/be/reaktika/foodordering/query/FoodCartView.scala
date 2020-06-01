package be.reaktika.foodordering.query

import java.util.UUID

import scala.collection.mutable

case class FoodCartViewResult(content: Option[FoodCartView])

case class FoodCartView(foodCartId: UUID, products: mutable.Map[UUID, Int]) {

  def addProducts(productId: UUID, amount: Int): Unit = {
    products.get(productId) match {
      case Some(quantity) =>
        products.update(productId, quantity + amount)
      case None =>
        products.update(productId, amount)
    }
  }

  def removeProducts(productId: UUID, amount: Int): Unit = {
    products.get(productId) match {
      case Some(quantity) if quantity - amount > 0 =>
        products.update(productId, quantity - amount)
      case _ =>
        products.remove(productId)
    }
  }

}

