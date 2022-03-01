package com.duckservice.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.duckservice.AbstractTest;
import com.duckservice.entities.Customer;
import com.duckservice.entities.Order;
import com.duckservice.entities.StandardRequestBody;
import com.duckservice.service.OrderService;
import com.duckservice.util.StandardJsonResponse;
import com.duckservice.util.StandardJsonResponseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class RestControllerTest extends AbstractTest{

    @Mock
    private RPriorityBlockingQueue<Order> priorityBlockingQueueMock;
    @MockBean
    private OrderService                  orderServiceMock;

    @Test
    public void testCreateOrderSuccess() throws Exception{

        given(this.orderServiceMock.createOrder(any(), anyLong())).willReturn(this.creteOrder());

        final Gson gson = new Gson();
        final String json = gson.toJson(this.createRequest());
        final MvcResult mvcResult = this.mvc
                .perform(
                        MockMvcRequestBuilders.post("/duckService/addOrder")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();
        final StandardJsonResponse standardJsonResponse = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), StandardJsonResponseImpl.class);
        Assert.assertEquals(201, standardJsonResponse.getHttpResponseCode());
        Assert.assertEquals("Added in queue", standardJsonResponse.getData().get("Order"));

    }

    @Test
    public void testCreateOrderFailCase() throws Exception{
        given(this.orderServiceMock.createOrder(any(), anyLong())).willReturn(this.creteOrder());

        final Gson gson = new Gson();
        final String json = gson.toJson(this.createRequest());
        this.mvc.perform(
                MockMvcRequestBuilders.post("/duckService/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();
        final MvcResult mvcResult = this.mvc
                .perform(
                        MockMvcRequestBuilders.post("/duckService/addOrder")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();
        final StandardJsonResponse standardJsonResponse = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), StandardJsonResponseImpl.class);
        Assert.assertEquals(429, standardJsonResponse.getHttpResponseCode());
    }

    @Test
    public void testAllOrderDetails() throws Exception{
        doNothing().when(this.orderServiceMock).getAllOrdersWithWaitTime(any(), anyMap());

        final Gson gson = new Gson();
        final String json = gson.toJson(this.createRequest());

        final MvcResult mvcResult = this.mvc
                .perform(
                        MockMvcRequestBuilders.get("/duckService/allOrdersDetails")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();
        final StandardJsonResponse standardJsonResponse = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), StandardJsonResponseImpl.class);
        Assert.assertEquals(200, standardJsonResponse.getHttpResponseCode());
    }

    @Test
    public void testNextOrder() throws Exception{

        final Gson gson = new Gson();
        final String json = gson.toJson(this.createRequest());

        final MvcResult mvcResult = this.mvc
                .perform(
                        MockMvcRequestBuilders.get("/duckService/nextOrder")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();
        final StandardJsonResponse standardJsonResponse = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), StandardJsonResponseImpl.class);
        Assert.assertEquals(200, standardJsonResponse.getHttpResponseCode());
    }

    @Test
    public void testDeleteOrder() throws Exception{

        final Gson gson = new Gson();
        final String json = gson.toJson(this.createRequest());

        final MvcResult mvcResult = this.mvc
                .perform(
                        MockMvcRequestBuilders.delete("/duckService/remove")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();
        final StandardJsonResponse standardJsonResponse = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), StandardJsonResponseImpl.class);
        Assert.assertEquals(200, standardJsonResponse.getHttpResponseCode());
    }

    @Test
    public void testOrderDetails() throws Exception{
        given(this.orderServiceMock.createOrder(any(), anyLong())).willReturn(this.creteOrder());

        final Gson gson = new Gson();
        final String json = gson.toJson(this.createRequest());
        final MvcResult mvcResult = this.mvc
                .perform(
                        MockMvcRequestBuilders.get("/duckService/orderDetails")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andReturn();
        final StandardJsonResponse standardJsonResponse = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), StandardJsonResponseImpl.class);
        Assert.assertEquals(404, standardJsonResponse.getHttpResponseCode());
    }

    private StandardRequestBody createRequest(){
        final StandardRequestBody jsonRequest = new StandardRequestBody();
        jsonRequest.setCustomerId(332255);
        jsonRequest.setOrderId(654L);
        jsonRequest.setQuantity(10);
        return jsonRequest;
    }

    private Order creteOrder(){
        final Customer customer = new Customer(332255);
        return new Order(10, customer, 654L);
    }

}
