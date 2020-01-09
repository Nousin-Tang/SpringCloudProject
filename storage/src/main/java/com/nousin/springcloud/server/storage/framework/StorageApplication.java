package com.nousin.springcloud.server.storage.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.nousin.springcloud")
@MapperScan("com.nousin.springcloud")
public class StorageApplication {

	@Value("${nousin.timezone:UTC}")
	private String timezone; // 时区

	@Value("${nousin.language:en_US}")
	private String language; // 语言

	@PostConstruct
	public void start(){
		TimeZone.setDefault(TimeZone.getTimeZone(timezone));
		Locale.setDefault(new Locale(language));
	}

	public static void main(String[] args) {
		SpringApplication.run(StorageApplication.class, args);
	}

}
