package com.gonnect.config.server.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.gonnect.config.server.AutoConfigureSmartConfigServer;

@SpringBootApplication
@AutoConfigureSmartConfigServer
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
