package com.duckservice.rest;

import java.util.HashMap;
import java.util.Optional;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.duckservice.entities.Order;
import com.duckservice.entities.StandardRequestBody;
import com.duckservice.service.OrderService;
import com.duckservice.util.StandardJsonResponse;
import com.duckservice.util.StandardJsonResponseImpl;
import com.duckservice.util.Validator;

@RestController
public class RuberDuckController{
    @Autowired
    private RPriorityBlockingQueue<Order> priorityBlockingQueue;

    @Autowired
    private RAtomicLong                   orderId;

    @Autowired
    @Qualifier("orderService")
    private OrderService                  orderService;

    @RequestMapping(value = "/echo/{name}", method = RequestMethod.GET)
    public String echo(@PathVariable("name") final String name){
        return "You said' " + name + "'";
    }

    /**
     * Add new order in queue
     * 
     * @param jsonRequest
     * @return StandardJsonResponse
     */
    @PostMapping("/duckService/addOrder")
    public StandardJsonResponse addOrder(@RequestBody final StandardRequestBody jsonRequest){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        if(Validator.validateCustomerIdAndQuantity(jsonRequest, responseData)){
            final Order order = this.orderService.createOrder(jsonRequest, this.orderId.incrementAndGet());
            if(this.priorityBlockingQueue.stream()
                    .anyMatch(o -> o.getCustomer().getId() == order.getCustomer().getId())){
                responseData.put("Order", "One order at a time per customer can be served");
                jsonResponse.setSuccess(false);
                jsonResponse.setHttpResponseCode(HttpStatus.TOO_MANY_REQUESTS.value());
            }else{
                this.priorityBlockingQueue.add(order);
                responseData.put("Order", "Added in queue");
                jsonResponse.setSuccess(true);
                jsonResponse.setHttpResponseCode(HttpStatus.CREATED.value());
            }
        }
        jsonResponse.setData(responseData);
        return jsonResponse;
    }

    /**
     * Get position of order and waiting time by providing order id in request
     * 
     * @param jsonRequest
     * @return StandardJsonResponse
     */
    @GetMapping("/duckService/orderDetails")
    public StandardJsonResponse getQueuePositionAndWaitTime(@RequestBody final StandardRequestBody jsonRequest){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        if(Validator.validateOrderId(jsonRequest, responseData)){
            final Optional<Order> order = this.priorityBlockingQueue.stream()
                    .filter(o -> o.getId() == jsonRequest.getOrderId())
                    .findFirst();
            if(order.isPresent()){
                this.orderService.checkIndexAndWaitTime(this.priorityBlockingQueue, order.get(), responseData);
                jsonResponse.setHttpResponseCode(HttpStatus.OK.value());
                jsonResponse.setSuccess(true);
            }else{
                responseData.put("Order", "Order not present in queue");
                jsonResponse.setSuccess(false);
                jsonResponse.setHttpResponseCode(HttpStatus.NOT_FOUND.value());
            }
        }
        jsonResponse.setData(responseData);
        return jsonResponse;
    }

    /**
     * 
     * Get details of all orders present in queue
     * 
     * @return StandardJsonResponse
     */
    @GetMapping("/duckService/allOrdersDetails")
    public StandardJsonResponse getAllOrdersAndWaitTime(){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        if(this.priorityBlockingQueue.isEmpty()){
            responseData.put("Order", "No orders in queue");
            jsonResponse.setSuccess(false);
            jsonResponse.setHttpResponseCode(HttpStatus.NOT_FOUND.value());
        }else{
            this.orderService.getAllOrdersWithWaitTime(this.priorityBlockingQueue, responseData);
            jsonResponse.setSuccess(true);
            jsonResponse.setHttpResponseCode(HttpStatus.OK.value());
        }
        jsonResponse.setData(responseData);
        return jsonResponse;
    }

    /**
     * Get next order
     * 
     * @param jsonRequest
     * @return StandardJsonResponse
     */
    @GetMapping("/duckService/nextOrder")
    public StandardJsonResponse nextDelivery(@RequestBody final StandardRequestBody jsonRequest){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        if(this.priorityBlockingQueue.isEmpty()){
            responseData.put("Order", "No orders present in the queue");
            jsonResponse.setSuccess(false);
            jsonResponse.setHttpResponseCode(HttpStatus.NOT_FOUND.value());
        }else{
            final Order order = this.priorityBlockingQueue.peek();
            responseData.put("Order", order);
            jsonResponse.setSuccess(true);
            jsonResponse.setHttpResponseCode(HttpStatus.OK.value());
        }

        jsonResponse.setData(responseData);
        return jsonResponse;
    }

    /**
     * Deletes the order for given customer id
     * 
     * @param jsonRequest
     * @return
     */
    @DeleteMapping("/duckService/remove")
    public StandardJsonResponse deleteOrder(@RequestBody final StandardRequestBody jsonRequest){
        final StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        final HashMap<String, Object> responseData = new HashMap<>();
        if(Validator.validateCustomerId(jsonRequest, responseData)){
            final Optional<Order> order = this.priorityBlockingQueue.stream()
                    .filter(o -> o.getCustomer().getId() == jsonRequest.getCustomerId())
                    .findFirst();

            if(order.isPresent()){
                this.orderService.remove(this.priorityBlockingQueue, order.get(), responseData);
                jsonResponse.setHttpResponseCode(HttpStatus.OK.value());
                jsonResponse.setSuccess(true);
            }else{
                responseData.put("Order", "Order not present in queue");
                jsonResponse.setSuccess(false);
                jsonResponse.setHttpResponseCode(HttpStatus.NOT_FOUND.value());
            }
        }
        jsonResponse.setData(responseData);
        return jsonResponse;
    }
}
