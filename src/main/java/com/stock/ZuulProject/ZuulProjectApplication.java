package com.stock.ZuulProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

import com.stock.authorization.AutorizationConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableSwagger2
@Import(AutorizationConfiguration.class)
public class ZuulProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulProjectApplication.class, args);
	}
}
