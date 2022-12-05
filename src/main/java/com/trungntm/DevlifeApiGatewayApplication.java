package com.trungntm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableEurekaClient
@EnableWebFlux
public class DevlifeApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevlifeApiGatewayApplication.class, args);
    }

}
