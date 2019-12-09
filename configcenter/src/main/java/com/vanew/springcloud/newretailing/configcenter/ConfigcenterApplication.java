package com.vanew.springcloud.newretailing.configcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication(scanBasePackages = "com.vanew.springcloud.newretailing")
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigcenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigcenterApplication.class, args);
	}

}
