package com.github.talbotgui.springboot.business.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Une configuration Spring-boot pour le test. Cette classe remplace le
 * traditionnel fichier XML.
 */
@SpringBootApplication
@EntityScan("com.github.talbotgui.springboot.business")
@EnableJpaRepositories("com.github.talbotgui.springboot.business")
@PropertySource("classpath:db-config.properties")
public class SpringApplicationForTests {

}
