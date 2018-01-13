package com.et;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * @EnableEurekaClient自动注册到注册中心
 * @author Administrator
 *
 */
@SpringBootApplication
public class Main {
	 public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
 
}
