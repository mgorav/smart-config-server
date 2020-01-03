package com.gonnect.sample.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.gonnect.config.server.mongodb.AutoConfigureSmartConfigServer;

@SpringBootApplication
@AutoConfigureSmartConfigServer
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
