package com.projectsync.iamuser.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Endpoint(id = "hi")
@Component
public class CustomEndpoint {

    @ReadOperation
    @Bean
    public String customMessage() {

        return "hello, world";
    }
}
