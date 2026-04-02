package com.webapp.tech_shop.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvTest {

    @Value("${DB_URL}")
    private String url;

    @PostConstruct
    public void test() {
        System.out.println("DB_URL = " + url);
    }
}