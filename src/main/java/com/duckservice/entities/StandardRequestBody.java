package com.duckservice.entities;

public class StandardRequestBody{

    private int customerId;

    private int quantity;

    public int getCustomerId(){
        return this.customerId;
    }

    public void setCustomerId(final int customerId){
        this.customerId = customerId;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(final int quantity){
        this.quantity = quantity;
    }
}
