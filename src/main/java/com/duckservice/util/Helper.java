package com.duckservice.util;

import com.duckservice.entities.Customer;
import com.duckservice.entities.Order;
import com.duckservice.entities.StandardRequestBody;

public class Helper{
    public static Order createOrder(final StandardRequestBody jsonRequest, final long orderId){
        final Customer customer = new Customer(jsonRequest.getCustomerId());
        return new Order(jsonRequest.getQuantity(), customer, orderId);
    }
}
