package be.reaktika.foodordering.query

import java.util.UUID

class FoodCartViewRepository {

  var catalog = Map.empty[UUID, FoodCartView]

  def findById(id: UUID): Option[FoodCartView] = {
    catalog.get(id)
  }

  def save(id: UUID, view: FoodCartView): Unit = {
    catalog = catalog + (id -> view)
  }

}
