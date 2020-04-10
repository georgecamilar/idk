package com.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan
public class PropConfiguration {
    
    @Bean
    public Properties props(){
        Properties props = new Properties();
        try {
            props.load(new FileReader("src/main/resources/bd.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
