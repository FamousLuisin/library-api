package com.noc.rest_api.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.noc.rest_api.model.Greeting;

@RestController
public class GreetingController {

    private static final String template = "Hello %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping(method = RequestMethod.GET, path = "/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "Application") String name){
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
