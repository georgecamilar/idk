package com.utils;

import com.controller.Service;
import com.controller.ServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@EnableWebMvc
@Configuration
public class AppConfig {

    @Bean
    public Service service() {
        Properties props = new Properties();
        try {
            props.load(new FileReader("src/main/resources/bd.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ServiceImplementation(props);
    }

    @Bean(name = "/MppService")
    public RemoteExporter exporter() {
        HttpInvokerServiceExporter serviceExporter = new HttpInvokerServiceExporter();
        serviceExporter.setService(service());
        serviceExporter.setServiceInterface(Service.class);
        return serviceExporter;
    }
}
