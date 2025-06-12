package com.zimnyciechan.eventice.events.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "public")
public class PublicRestController {

    @GetMapping("time")
    public String getTime() {
        return new Date().toString();
    }
}
