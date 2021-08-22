package com.zst.backendserver.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class BackendController {
    @GetMapping("/api1")
    public Mono<String> api1(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String json = JSON.toJSONString(headers.toSingleValueMap());
        return Mono.just(json);
    }

    @GetMapping("/api2")
    public Mono<String> api2() {
        return Mono.just("This is api2");
    }
}
