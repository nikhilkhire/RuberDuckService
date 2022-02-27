package com.duckservice.rest;

import java.util.HashMap;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.duckservice.entities.Order;
import com.duckservice.entities.StandardRequestBody;
import com.duckservice.util.Helper;
import com.duckservice.util.StandardJsonResponse;
import com.duckservice.util.StandardJsonResponseImpl;
import com.duckservice.util.Validator;

@RestController
@RequestMapping("/duckService")
public class RestConstoller{
    @Autowired
    private RPriorityBlockingQueue<Order> priorityBlockingQueue;

    @Autowired
    private RAtomicLong                   orderId;

    @RequestMapping(value = "/echo/{name}", method = RequestMethod.GET)
    public String echo(@PathVariable("name") final String name){
        return "You said' " + name + "'";
    }

    @PostMapping("/addOrder")
    public StandardJsonResponse addOrder(@RequestBody final StandardRequestBody jsonRequest){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        if(Validator.validateCustomerIdAndQuantity(jsonRequest, responseData)){
            final Order order = Helper.createOrder(jsonRequest, this.orderId.incrementAndGet());
            if(this.priorityBlockingQueue.stream()
                    .anyMatch(o -> o.getCustomer().getId() == order.getCustomer().getId())){
                responseData.put("Order", "One order at a time per customer can be served");
                jsonResponse.setSuccess(false);
            }else{
                this.priorityBlockingQueue.add(order);
                responseData.put("Order", "Added in queue");
                jsonResponse.setSuccess(true);
            }
        }
        jsonResponse.setData(responseData);
        return jsonResponse;
    }

    public StandardJsonResponse getQueuePositionAndWaitTime(){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        // throw new IllegalStateException("Not yet implemented");
        return jsonResponse;
    }

    public StandardJsonResponse getAllOrdersAndWaitTime(){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        // throw new IllegalStateException("Not yet implemented");
        return jsonResponse;
    }

    @GetMapping("/nextOrder")
    public StandardJsonResponse nextDelivery(){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        final Order order = this.priorityBlockingQueue.peek();
        // throw new IllegalStateException("Not yet implemented");
        responseData.put("Order", order);
        jsonResponse.setSuccess(true);
        jsonResponse.setData(responseData);
        return jsonResponse;
    }

    public StandardJsonResponse deleteOrder(){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        // throw new IllegalStateException("Not yet implemented");
        return jsonResponse;
    }
}
