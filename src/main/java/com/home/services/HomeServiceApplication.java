package com.home.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HomeServiceApplication extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder)
    {
        return builder.sources(HomeServiceApplication.class);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(HomeServiceApplication.class , args);
    }

}
