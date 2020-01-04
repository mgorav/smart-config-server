package com.gonnect.config.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gonnect.config.server.config.SmartConfigServerConfigurer;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Add this annotation to a {@code @Configuration} class to enable Spring Cloud Config
 * Server backed by MongoDB.
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(SmartConfigServerConfigurer.class)
@EnableConfigServer
public @interface AutoConfigureSmartConfigServer {

}
