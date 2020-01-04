package com.gonnect.config.server.repository;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.gonnect.config.server.AutoConfigureSmartConfigServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;


public class SmartServerRepositoryTests {

	private ConfigurableApplicationContext context;

	@Before
	public void init() {

	}

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void defaultRepo() {
		// Prepare context
		Map<String, Object> props = new HashMap<>();
		props.put("spring.data.mongodb.database", "testdb");
		context = new SpringApplicationBuilder(TestConfiguration.class).web(WebApplicationType.NONE).properties(props).run();
		// Prepare test
		MongoTemplate mongoTemplate = this.context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection("testapp");
		SmartConfigServerRepository.MongoPropertySource ps = new SmartConfigServerRepository.MongoPropertySource();
		ps.getSource().put("testkey", "testval");
		mongoTemplate.save(ps, "testapp");
		// Test
		EnvironmentRepository repository = this.context.getBean(EnvironmentRepository.class);
		Environment environment = repository.findOne("testapp", "default", null);
		assertEquals("testapp-default", environment.getPropertySources().get(0).getName());
		assertEquals(1, environment.getPropertySources().size());
		assertEquals(true, environment.getPropertySources().get(0).getSource().containsKey("testkey"));
		assertEquals("testval", environment.getPropertySources().get(0).getSource().get("testkey"));
	}

	@Test
	public void nestedPropertySource() {
		// Prepare context
		Map<String, Object> props = new HashMap<>();
		props.put("spring.data.mongodb.database", "testdb");
		context = new SpringApplicationBuilder(TestConfiguration.class).web(WebApplicationType.NONE).properties(props).run();
		// Prepare test
		MongoTemplate mongoTemplate = this.context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection("testapp");
		SmartConfigServerRepository.MongoPropertySource ps = new SmartConfigServerRepository.MongoPropertySource();
		Map<String, String> inner = new HashMap<String, String>();
		inner.put("inner", "value");
		ps.getSource().put("outer", inner);
		mongoTemplate.save(ps, "testapp");
		// Test
		EnvironmentRepository repository = this.context.getBean(EnvironmentRepository.class);
		Environment environment = repository.findOne("testapp", "default", null);
		assertEquals("testapp-default", environment.getPropertySources().get(0).getName());
		assertEquals(1, environment.getPropertySources().size());
		assertEquals(true, environment.getPropertySources().get(0).getSource().containsKey("outer.inner"));
		assertEquals("value", environment.getPropertySources().get(0).getSource().get("outer.inner"));
	}

	@Test
	public void repoWithProfileAndLabelInSource() {
		// Prepare context
		Map<String, Object> props = new HashMap<>();
		props.put("spring.data.mongodb.database", "testdb");
		context = new SpringApplicationBuilder(TestConfiguration.class).web(WebApplicationType.NONE).properties(props).run();
		// Prepare test
		MongoTemplate mongoTemplate = this.context.getBean(MongoTemplate.class);
		mongoTemplate.dropCollection("testapp");
		SmartConfigServerRepository.MongoPropertySource ps = new SmartConfigServerRepository.MongoPropertySource();
		ps.setProfile("confprofile");
		ps.setLabel("conflabel");
		ps.getSource().put("profile", "sourceprofile");
		ps.getSource().put("label", "sourcelabel");
		mongoTemplate.save(ps, "testapp");
		// Test
		EnvironmentRepository repository = this.context.getBean(EnvironmentRepository.class);
		Environment environment = repository.findOne("testapp", "confprofile", "conflabel");
		assertEquals(1, environment.getPropertySources().size());
		assertEquals("testapp-confprofile-conflabel", environment.getPropertySources().get(0).getName());
		assertEquals(true, environment.getPropertySources().get(0).getSource().containsKey("profile"));
		assertEquals("sourceprofile", environment.getPropertySources().get(0).getSource().get("profile"));
		assertEquals(true, environment.getPropertySources().get(0).getSource().containsKey("label"));
		assertEquals("sourcelabel", environment.getPropertySources().get(0).getSource().get("label"));
	}

	@Configuration
	@AutoConfigureSmartConfigServer
	@Import({
		PropertyPlaceholderAutoConfiguration.class,
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
	})
	protected static class TestConfiguration {

	}

}
