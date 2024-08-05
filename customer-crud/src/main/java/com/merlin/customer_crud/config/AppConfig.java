package com.merlin.customer_crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    //Defines a bean. Used to fetch from remote sunbase api
    @Bean
    public RestTemplate restTemplate(){
        //Creates and returns a new instance of RestTemplate
        return new RestTemplate();
    }
}
