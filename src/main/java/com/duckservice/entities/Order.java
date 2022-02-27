package com.duckservice.entities;

import java.io.Serializable;

public class Order implements Comparable<Order>, Serializable{
    private static final long serialVersionUID = -773059248164293315L;
    private Long              id;
    private int               _quantity;
    private Customer          customer;
    private int               maxWaitingTime;
    private int               arrivalTime;

    public Order(){

    }

    public Order(final int quantity, final Customer customer, final long orderId){
        this.id = orderId;
        this._quantity = quantity;
        this.customer = customer;
    }

    public Long getId(){
        return this.id;
    }

    public int getQuantity(){
        return this._quantity;
    }

    public Customer getCustomer(){
        return this.customer;
    }

    public int getMaxWaitingTime(){
        return this.maxWaitingTime;
    }

    public void setMaxWaitingTime(final int maxWaitingTime){
        this.maxWaitingTime = maxWaitingTime;
    }

    @Override
    public String toString(){
        return "Order{Order Id= " + this.getId() + ", quantity=" + this._quantity + ", Customer Number="
                + this.getCustomer().getId() + '}';
    }

    @Override
    public int compareTo(final Order o){
        if(o.getCustomer().isPremium() == this.getCustomer().isPremium()){
            return o.getId().compareTo(this.getId());
        }
        return 1;
    }
}
