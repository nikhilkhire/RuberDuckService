package com.duckservice.entities;

import java.io.Serializable;

public class Order implements Comparable<Order>, Serializable{
    private static final long serialVersionUID = -773059248164293315L;
    private Long              id;
    private Integer           quantity;
    private Customer          customer;
    private Integer           maxWaitingTime;
    private Long              arrivalTime;

    public Order(){

    }

    public Order(final Integer quantity, final Customer customer, final Long orderId){
        this.id = orderId;
        this.quantity = quantity;
        this.customer = customer;
        this.arrivalTime = System.currentTimeMillis();
    }

    public Long getId(){
        return this.id;
    }

    public Integer getQuantity(){
        return this.quantity;
    }

    public Customer getCustomer(){
        return this.customer;
    }

    public Integer getMaxWaitingTime(){
        return this.maxWaitingTime;
    }

    public void setMaxWaitingTime(final Integer maxWaitingTime){
        this.maxWaitingTime = maxWaitingTime;
    }

    @Override
    public String toString(){
        return "Order{Order Id= " + this.getId() + ", quantity=" + this.quantity + ", Customer Number="
                + this.getCustomer().getId() + ", Wait Time=" + this.maxWaitingTime + '}';
    }

    @Override
    public int compareTo(final Order o){
        if(o.getCustomer().isPremium() == this.getCustomer().isPremium()){
            return o.getId().compareTo(this.getId());
        }
        return 1;
    }
}
