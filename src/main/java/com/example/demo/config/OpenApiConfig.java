package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Image Upload and View API")
                        .version("1.0")
                        .description("A simple API for uploading and viewing images with Azure SQL storage")
                        .contact(new Contact()
                                .name("Example Corp")
                                .email("contact@example.com")));
    }
} 