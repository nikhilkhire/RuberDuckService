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
        int quantity = 0;
        for(int i = 0; i < this.priorityBlockingQueue.size(); i++){

            final Order order = this.priorityBlockingQueue.peek();
            quantity += order.getQuantity();
            if(quantity < 25){
                this.priorityBlockingQueue.poll();
                System.out.println(order.toString());
            }else{
                break;
            }

        }

    }

}