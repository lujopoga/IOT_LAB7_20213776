package com.laboratorio.registroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class RegistroServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistroServiceApplication.class, args);
    }
}
