package com.gonnect.config.server.config;

import com.gonnect.config.server.repository.SmartConfigServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class SmartConfigServerConfigurer {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	public EnvironmentRepository environmentRepository() {
		return new SmartConfigServerRepository(mongoTemplate);
	}

}
