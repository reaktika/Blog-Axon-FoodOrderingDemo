# Food Ordering Demo - Scala - Axon
Based on the demo application delivered by Axon in [Java](https://github.com/AxonIQ/food-ordering-demo)

## Running the application
Start the Axon server:
```
> docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver
```

Alternatively, an in-memory event-store can be used.
This can be configured in FoodOrderingApplication by removing the comments:
```
    //.configureEmbeddedEventStore(_ => new InMemoryEventStorageEngine)
```

Next up, running the Axon application using sbt:
```
> sbt run
```

## Using the application
Request all food carts:
```
> curl -H "Content-Type: application/json" -X GET http://localhost:8080/foodCarts
```

Create a new cart:
```
> curl -H "Content-Type: application/json" -X POST http://localhost:8080/foodCart/create
```

Add an item to the cart:
```
> curl -H "Content-Type: application/json" -X POST http://localhost:8080/foodCart/{generatedCartId}/select/{someProductUUID}/quantity/10
```

Overview of the cart:
```
> curl -H "Content-Type: application/json" -X GET http://localhost:8080/foodCart/{generatedCartId}
```
