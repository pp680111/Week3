package com.zst.backendserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BackendController {
    @GetMapping("/api1")
    public Mono<String> api1() {
        return Mono.just("This is api1");
    }

    @GetMapping("/api2")
    public Mono<String> api2() {
        return Mono.just("This is api2");
    }
}
