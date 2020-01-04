package com.gonnect.config.server;

import com.gonnect.config.server.repository.SmartConfigServerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@RunWith(SpringRunner.class)
@SpringBootTest(
	classes = SmartConfigServerTests.MongoConfigServerApplication.class,
	webEnvironment = RANDOM_PORT
)
public class SmartConfigServerTests {

	@LocalServerPort
	private int port;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void server() {
		mongoTemplate.dropCollection("testapp");
		SmartConfigServerRepository.MongoPropertySource ps = new SmartConfigServerRepository.MongoPropertySource();
		ps.getSource().put("testkey", "testval");
		mongoTemplate.save(ps, "testapp");
		Environment environment = new TestRestTemplate().getForObject("http://localhost:"
				+ port + "/testapp/default", Environment.class);
		assertEquals("testapp-default", environment.getPropertySources().get(0).getName());
		assertEquals(1, environment.getPropertySources().size());
		assertEquals(true, environment.getPropertySources().get(0).getSource().containsKey("testkey"));
		assertEquals("testval", environment.getPropertySources().get(0).getSource().get("testkey"));
	}

	@SpringBootApplication
	@AutoConfigureSmartConfigServer
	public static class MongoConfigServerApplication {

		public static void main(String args[]) {
			SpringApplication.run(MongoConfigServerApplication.class, args);
		}

	}

}
