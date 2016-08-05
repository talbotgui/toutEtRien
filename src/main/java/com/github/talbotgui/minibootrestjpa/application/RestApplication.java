package com.github.talbotgui.minibootrestjpa.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;

/**
 * Une configuration Spring-boot pour l'application. Cette classe remplace le
 * traditionnel fichier XML.
 */
@SpringBootApplication
@EntityScan({ RestApplication.ENTITY_SCAN })
@ComponentScan({ RestApplication.COMPONENT_SCAN_WEB, RestApplication.COMPONENT_SCAN_SRV })
@EnableJpaRepositories(RestApplication.JPA_REPOSITORIES)
@PropertySource(RestApplication.PROPERTY_SOURCE)
public class RestApplication {

	private static ApplicationContext ac;

	public static final String COMPONENT_SCAN_SRV = "com.github.talbotgui.minibootrestjpa.metier.service";
	public static final String COMPONENT_SCAN_WEB = "com.github.talbotgui.minibootrestjpa.rest.controleur";
	public static final String ENTITY_SCAN = "com.github.talbotgui.minibootrestjpa.metier.entities";
	public static final String JPA_REPOSITORIES = "com.github.talbotgui.minibootrestjpa.metier.dao";
	public static final String PROPERTY_SOURCE = "classpath:db-config-prod.properties";

	public static ApplicationContext getApplicationContext() {
		return ac;
	}

	public static void main(final String[] args) {
		start();
	}

	public static void start() {
		ac = SpringApplication.run(RestApplication.class);
	}

	public static void stop() {
		if (ac != null) {
			SpringApplication.exit(ac);
		}
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return (container -> {

			// Error pages
			final ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
			final ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
			final ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
			container.addErrorPages(error401Page, error404Page, error500Page);

			// Mime types
			final MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
			container.setMimeMappings(mappings);
		});
	}
}
