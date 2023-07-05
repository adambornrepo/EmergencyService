package com.tech.configuration;

import com.tech.payload.response.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ApiResponse apiResponse(){
        return new ApiResponse();
    }

}
