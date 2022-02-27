package com.duckservice.service;

import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.duckservice.entities.Order;

@Service
public class Executor{

    @Autowired
    private RPriorityBlockingQueue<Order> priorityBlockingQueue;

    @Scheduled(fixedDelay = 50000)
    public void runTask() throws InterruptedException{
        System.out.println("----------------------------------------");
        final Order order = this.priorityBlockingQueue.take();
        // this.priorityBlockingQueue.stream().forEach(System.out::println);
        System.out.println("Customer Id : " + order.getCustomer().getId());
        System.out.println("Order Id : " + order.getId());
        System.out.println("Quantity : " + order.getQuantity());
        System.out.println("Priority : " + order.getCustomer().isPremium());
    }

}