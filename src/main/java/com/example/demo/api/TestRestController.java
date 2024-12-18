package com.example.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

    @GetMapping("test")
    public String testRequest() {
        return "Hello World!, dupa biskupa";
    }
}