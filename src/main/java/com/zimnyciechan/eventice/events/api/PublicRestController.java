package com.zimnyciechan.eventice.events.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "public")
public class PublicRestController {

    @Value("${eventice.frontendUrl}")
    private String frontendUrl;

    @GetMapping("time")
    public String getTime() {
        return new Date().toString();
    }

    @GetMapping("front")
    public String getFrontUrl() {
        return frontendUrl;
    }
}
