package com.duckservice.util;

import java.util.HashMap;
import java.util.Map;

import com.duckservice.entities.StandardRequestBody;

public class Validator{

    public static boolean validateRequest(
            final StandardRequestBody jsonRequest,
            final HashMap<String, Object> responseData){

        if(jsonRequest == null){
            responseData.put("Request", "Can not be null");
            return false;
        }
        return true;
    }

    public static boolean validateCustomerIdAndQuantity(
            final StandardRequestBody jsonRequest,
            final HashMap<String, Object> responseData){

        if(Validator.validateRequest(jsonRequest, responseData)){
            if(!(jsonRequest.getQuantity() > 0 && jsonRequest.getQuantity() < 25)){
                responseData.put("Quantity", "Invalid");
                return false;
            }
            return Validator.isCustomerIdValid(jsonRequest, responseData);
        }
        return false;
    }

    public static boolean validateOrderId(
            final StandardRequestBody jsonRequest,
            final HashMap<String, Object> responseData){

        if(Validator.validateRequest(jsonRequest, responseData)){
            if(!(jsonRequest.getOrderId() > 0)){
                responseData.put("Order Id", "Invalid");
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean validateCustomerId(
            final StandardRequestBody jsonRequest,
            final HashMap<String, Object> responseData){

        return Validator.validateRequest(jsonRequest, responseData)
                && Validator.isCustomerIdValid(jsonRequest, responseData);
    }

    private static boolean isCustomerIdValid(
            final StandardRequestBody jsonRequest,
            final Map<String, Object> responseData){
        if(!(jsonRequest.getCustomerId() > 0)){
            responseData.put("CustomerId", "Invalid");
            return false;
        }
        return true;
    }

}
