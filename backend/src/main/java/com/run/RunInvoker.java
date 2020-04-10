package com.run;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunInvoker {
    public static void main(String[] args){
//        System.setProperty("java.rmi.server.hostname","192.168.100.22");
        ApplicationContext contextFactory = new ClassPathXmlApplicationContext("classpath:exporter.xml"); 
    }
}
