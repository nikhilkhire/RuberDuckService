package com.duckservice;

import java.io.File;
import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;

import com.duckservice.entities.Order;

@EnableScheduling
@SpringBootApplication
public class RuberDuckService{

    public static void main(final String[] args){
        SpringApplication.run(RuberDuckService.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException{
        final String configFileName = "redis.yml";
        final File resourceURL = ResourceUtils.getFile("classpath:" + configFileName);
        final Config config = Config.fromYAML(resourceURL);
        return Redisson.create(config);
    }

    @Bean
    public RPriorityBlockingQueue<Order> getQueue() throws IOException{
        return this.redissonClient().getPriorityBlockingQueue("order");
    }

    @Bean
    public RAtomicLong getLong() throws IOException{
        return this.redissonClient().getAtomicLong("orderId");
    }
}
