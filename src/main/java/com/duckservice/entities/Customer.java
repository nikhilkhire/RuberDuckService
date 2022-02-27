package com.duckservice.entities;

public class Customer{
    private static final int PREMIUM_CUSTOMER_BORDER = 1000;

    private boolean          premium;

    private int              id;

    public Customer(){}

    public Customer(final int id){
        this.id = id;
        this.premium = id < Customer.PREMIUM_CUSTOMER_BORDER;
    }

    public int getId(){
        return this.id;
    }

    public boolean isPremium(){
        return this.premium;
    }

    @Override
    public String toString(){
        return "Customer{premium=" + this.premium + ", id=" + this.id + '}';
    }
}
