# Priority Queue

## Situation
Jane and Joe work in a storehouse of a rubber duck lender. They are responsible for bringing rubber ducks to the front desk. But the manager is not satisfied with service the storehouse is providing to the front desk because either it takes too long before a delivery arrives or only a few items arrive. Their manager wants to fix this and asks them to write a web service to accept the orders and provides a list of items to deliver to the front desk. Desperate they reach their friend who is working in the IT department ... and your phone rings. Can you help them?

## Problem Statement
Jane and Joe explain to you that he got a bunch of requirements for the priority queue and the web service. The constraints he was given are:

* The service should implement a RESTful service
* All orders will be placed in a single queue
* Each order is comprised by the ID of the client and the requested quantity
* A client can only place one order and existing orders cannot be modified
* Client ID’s are in the range of 1 to 20000 and customers with ID’s less than 1000 are premium customers
* Orders are sorted by the number of seconds they are in the queue
* Orders from premium customers always have a higher priority than normal customers

They are supposed to look at the queue every 5 minutes and bring as many orders to the front desk as possible. Their cart is able to carry 25 rubber ducks and they should put as many orders as possible into their cart without skipping, changing or splitting any orders.

## Requirements
This leads to the following list of requirements for the endpoints:

* An endpoint for adding items to the queue. This endpoint must take two parameters, the ID of the client and the quantity
* An endpoint for the client where he can check his queue position and approximate wait time. Counting starts at 1.
* An endpoint which allows his manager to see all entries in the queue with the approximate wait time
* An endpoint to retrieve his next delivery which should be placed in the cart
* An endpoint to cancel an order. This endpoint should accept only the client ID

Endpoints should follow REST best practices, parameters should be passed in the fashion that is most closely matched by REST principles.
While you investigate the requirements, you notice that your Order Management system already has a class for `Orders` and `Customers`. You decide to reuse those to get started.

## Next Steps
Before you meet with Jane and Joe to discuss the REST services, you need to prepare the Priority Queue which allows you to process the orders. In your mind you already see the five different REST endpoints methods which you want to create together:

* addOrder
* getQueuePositionAndWaitTime
* getAllOrdersAndWaitTime
* nextDelivery
* deleteOrder
 
When meeting Jane and Joe you plan to fletch out the REST interface based on this skeleton.


