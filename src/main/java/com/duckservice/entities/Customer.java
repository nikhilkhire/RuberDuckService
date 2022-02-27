package com.duckservice.entities;

public class Customer{
    private static final int PREMIUM_CUSTOMER_BORDER = 1000;

    private boolean          _premium;

    private int              _id;

    public Customer(){}

    public Customer(final int id){
        this._id = id;
        this._premium = id < Customer.PREMIUM_CUSTOMER_BORDER;
    }

    public int getId(){
        return this._id;
    }

    public boolean isPremium(){
        return this._premium;
    }

    @Override
    public String toString(){
        return "Customer{premium=" + this._premium + ", id=" + this._id + '}';
    }
}
