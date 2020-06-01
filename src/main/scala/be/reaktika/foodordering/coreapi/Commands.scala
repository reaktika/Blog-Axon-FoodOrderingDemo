package be.reaktika.foodordering.coreapi

import java.util.UUID

import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier

import scala.annotation.meta.field

object Commands {

  case class CreateFoodCartCommand(
                                    @(RoutingKey @field) foodCartId: UUID
                                  )

  case class SelectProductCommand(
                                   @(TargetAggregateIdentifier @field) foodCartId: UUID,
                                   productId: UUID,
                                   quantity: Int
                                 )

  case class DeselectProductCommand(
                                     @(TargetAggregateIdentifier @field) foodCartId: UUID,
                                     productId: UUID,
                                     quantity: Int
                                   )

  case class ConfirmOrderCommand(
                                  @(TargetAggregateIdentifier @field) foodCartId: UUID
                                )
}

