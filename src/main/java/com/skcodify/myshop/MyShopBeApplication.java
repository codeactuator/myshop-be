package com.skcodify.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyShopBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyShopBeApplication.class, args);
	}
}
