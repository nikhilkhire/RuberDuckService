package com.duckservice;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.duckservice.rest.RestController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(RestController.class)
public abstract class AbstractTest{
    @Autowired
    protected MockMvc mvc;

}
