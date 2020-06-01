package be.reaktika.foodordering.coreapi

import java.util.UUID

object Queries {
  case class FindFoodCartQuery(foodCartId: UUID)
  case class FindFoodCartsQuery()
}
