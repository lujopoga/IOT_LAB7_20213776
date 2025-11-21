package com.laboratorio.lab7iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;



@SpringBootApplication
@EnableEurekaServer
public class Lab7IotApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab7IotApplication.class, args);
    }
}
