package com.duckservice.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.duckservice.AbstractTest;
import com.duckservice.entities.Customer;
import com.duckservice.entities.Order;
import com.duckservice.entities.StandardRequestBody;

public class RestControllerTest extends AbstractTest{

    @Mock
    private RPriorityBlockingQueue<Order> priorityBlockingQueue;
    @Mock
    private TestRestTemplate              restTemplate;

    @Test
    public void testCreateOrder() throws Exception{

        // given(Validator.validateCustomerIdAndQuantity(any(), any())).willReturn(true);
        // given(this.orderService.createOrder(any(), any())).willReturn(this.creteOrder());

        // final Gson gson = new Gson();
        // final String json = gson.toJson(this.createRequest());
        // final MvcResult mvcResult =
        // this.mvc.perform(MockMvcRequestBuilders.post("/duckService/addOrder").content(json))
        // .andReturn();
        // System.out.println(mvcResult.getResponse().toString());
        // Assert.assertEquals(404, status);
        // final StandardJsonResponse standardJsonResponse = (StandardJsonResponse) this.restTemplate
        // .postForEntity("/duckService/addOrder", json, StandardJsonResponse.class);
        // this.mvc.perform(post("/duckService/addOrder").contentType(MediaType.APPLICATION_JSON).content(json))
        // .andExpect(status().isCreated());

        // Assert.assertEquals("Added in queue", standardJsonResponse.getData().get("Order"));
    }

    private StandardRequestBody createRequest(){
        final StandardRequestBody jsonRequest = new StandardRequestBody();
        jsonRequest.setCustomerId(123456);
        jsonRequest.setOrderId(654L);
        jsonRequest.setQuantity(111);
        return jsonRequest;
    }

    private Order creteOrder(){
        final Customer customer = new Customer(123456);
        return new Order(111, customer, 654L);
    }

}
