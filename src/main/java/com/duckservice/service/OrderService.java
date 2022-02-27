package com.duckservice.service;

import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.stereotype.Service;

import com.duckservice.entities.Customer;
import com.duckservice.entities.Order;
import com.duckservice.entities.StandardRequestBody;

@Service("orderService")
public class OrderService{
    public Order createOrder(final StandardRequestBody jsonRequest, final long orderId){
        final Customer customer = new Customer(jsonRequest.getCustomerId());
        return new Order(jsonRequest.getQuantity(), customer, orderId);
    }

    public void checkIndexAndWaitTime(
            final RPriorityBlockingQueue<Order> priorityBlockingQueue,
            final Order order,
            final Map<String, Object> responseData){
        final PriorityBlockingQueue<Order> queue = new PriorityBlockingQueue<>(priorityBlockingQueue);
        Integer index = 0;
        Integer sumOfQuantity = 0;
        for(int i = 0; i < queue.size(); i++){
            final Order o = queue.poll();
            sumOfQuantity += o.getQuantity();
            if(o.getId() == order.getId()){
                index++;
            }
        }
        if(index == 0){
            responseData.put("Order", "Order is no more present in queue");

        }
        // Sum of all previous order divided by 25 which is orders served can be served together multiply by 5 which is
        // minutes to serve
        final Integer waitingTime = (sumOfQuantity / 25) * 5;
        responseData.put(
                "Order",
                "Order id " + order.getId() + " is at " + index + " position and max waiting time is " + waitingTime
                        + " minutes");

    }

    public void getAllOrdersWithWaitTime(
            final RPriorityBlockingQueue<Order> priorityBlockingQueue,
            final Map<String, Object> responseData){
        Integer sumOfQuantity = 0;
        // Make a copy of original queue
        final PriorityBlockingQueue<Order> queue = new PriorityBlockingQueue<>(priorityBlockingQueue);
        // Iterate over copy of original queue and retrieve each element to find waiting time and details.
        while(!queue.isEmpty()){
            final Order order = queue.poll();
            sumOfQuantity += order.getQuantity();
            order.setMaxWaitingTime((sumOfQuantity / 25) * 5);
            responseData.put(order.getId().toString(), order);
        }
    }

    public void remove(
            final RPriorityBlockingQueue<Order> priorityBlockingQueue,
            final Order order,
            final Map<String, Object> responseData){

        // Helper queue to store the elements
        // temporarily.
        final PriorityBlockingQueue<Order> ref = new PriorityBlockingQueue<>();
        final int s = priorityBlockingQueue.size();
        int cnt = 0;

        // Finding the value to be removed
        while(!priorityBlockingQueue.isEmpty() && priorityBlockingQueue.peek().getId() != order.getId()){
            ref.add(priorityBlockingQueue.peek());
            priorityBlockingQueue.remove();
            cnt++;
        }

        // If element is not found
        if(priorityBlockingQueue.isEmpty()){
            while(!ref.isEmpty()){

                // Pushing all the elements back into q
                priorityBlockingQueue.add(ref.peek());
                ref.remove();
            }
        }

        // If element is found
        else{
            responseData.put(priorityBlockingQueue.peek().getId().toString(), priorityBlockingQueue.peek());
            // Remove matching element
            priorityBlockingQueue.remove();
            while(!ref.isEmpty()){

                // Pushing all the elements back into q
                priorityBlockingQueue.add(ref.peek());
                ref.remove();
            }
            int k = s - cnt - 1;
            while(k-- > 0){

                // Pushing elements from front of q to its back
                final Order o = priorityBlockingQueue.peek();
                priorityBlockingQueue.remove();
                priorityBlockingQueue.add(o);
            }
        }
    }
}
