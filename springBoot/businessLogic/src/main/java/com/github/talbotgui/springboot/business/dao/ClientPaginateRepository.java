package com.github.talbotgui.springboot.business.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.talbotgui.springboot.business.entities.Client;

/**
 * Repository pour l'entité Client (Spring Data JPA).
 *
 * @see http://docs.spring.io/spring-data/commons/docs/current/api/org/
 *      springframework/data/repository/PagingAndSortingRepository.html
 * @see http://docs.spring.io/spring-data/commons/docs/current/api/org/
 *      springframework/data/repository/CrudRepository.html
 * @see http://docs.spring.io/spring-data/commons/docs/current/api/org/
 *      springframework/data/repository/Repository.html
 */
public interface ClientPaginateRepository extends PagingAndSortingRepository<Client, Long> {
}
