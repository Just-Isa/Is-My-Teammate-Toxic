package com.kilic.tfttracker.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// Unsure if necessary, we'll see

@RestController
@RequestMapping("tft")
public class ApiRequestController {

    Logger logger = LoggerFactory.getLogger(ApiRequestController.class);

    @GetMapping("/matchhistory/{user}")
    public @ResponseBody void getUserHistory(@PathVariable String username) {
        logger.info("{}", username);
    }
}
