package be.reaktika.foodordering.command

import java.util.UUID

import be.reaktika.foodordering.coreapi.Commands.{ConfirmOrderCommand, CreateFoodCartCommand, DeselectProductCommand, SelectProductCommand}
import be.reaktika.foodordering.coreapi.Events.{FoodCartCreatedEvent, OrderConfirmedEvent, ProductDeselectedEvent, ProductSelectedEvent}
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.{AggregateIdentifier, AggregateLifecycle}

class FoodCart() {

  @AggregateIdentifier private var foodCartId: UUID = _
  private var selectedProducts = Map.empty[UUID, Int]
  private var confirmed = false

  @CommandHandler
  def this(c: CreateFoodCartCommand) {
    this()
    AggregateLifecycle.apply(FoodCartCreatedEvent(c.foodCartId))
  }

  @CommandHandler
  def handle(c: SelectProductCommand): Unit = {
    AggregateLifecycle.apply(ProductSelectedEvent(c.foodCartId, c.productId, c.quantity))
  }

  @CommandHandler
  def handle(c: DeselectProductCommand): Unit = {
    AggregateLifecycle.apply(ProductDeselectedEvent(c.foodCartId, c.productId, c.quantity))
  }

  @CommandHandler
  def handle(c: ConfirmOrderCommand): Unit = {
    AggregateLifecycle.apply(OrderConfirmedEvent(c.foodCartId))
  }

  @EventSourcingHandler
  def on(e: FoodCartCreatedEvent): Unit = {
    foodCartId = e.foodCartId
    selectedProducts = Map.empty
    confirmed = false
  }

  @EventSourcingHandler
  def on(e: ProductSelectedEvent): Unit = {
    selectedProducts = selectedProducts.updatedWith(e.productId) {
      case Some(quantity) =>
        Some(e.quantity + quantity)
      case None =>
        Some(e.quantity)
    }
  }

  @EventSourcingHandler
  def on(e: ProductDeselectedEvent): Unit = {
    selectedProducts = selectedProducts.updatedWith(e.productId) {
      case Some(quantity) if quantity - e.quantity > 0 =>
        Some(e.quantity - quantity)
      case _ =>
        Some(0)
    }
  }

  @EventSourcingHandler
  def on(e: OrderConfirmedEvent): Unit = {
    confirmed = true
  }

}
