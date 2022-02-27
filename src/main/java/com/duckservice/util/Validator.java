package com.duckservice.util;

import java.util.HashMap;

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
            if(!(jsonRequest.getQuantity() > 0)){
                responseData.put("Quantity", "Invalid");
                return false;
            }
            if(!(jsonRequest.getCustomerId() > 0)){
                responseData.put("CustomerId", "Invalid");
                return false;
            }
            return true;
        }
        return false;
    }

}
