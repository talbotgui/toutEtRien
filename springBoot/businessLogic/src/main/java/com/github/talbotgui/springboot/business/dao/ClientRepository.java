package com.github.talbotgui.springboot.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.springboot.business.entities.Client;

/**
 * Repository pour l'entité Client (Spring Data JPA).
 *
 * @see http://docs.spring.io/spring-data/jpa/docs/current/reference/html/# repositories.query-methods
 *
 * @see http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/
 *      PagingAndSortingRepository.html
 * @see http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.
 *      html
 * @see http://docs.spring.io/spring-data/commons/docs/current/api/org/ springframework/data/repository/Repository.html
 */
public interface ClientRepository extends CrudRepository<Client, Long> {

	@Query("select c from Client c where (nom=:nom or :nom is null) and (prenom=:prenom or :prenom is null)")
	List<Client> findByNomAndPrenom(@Param("nom") String nom, @Param("prenom") String prenom);

	List<Client> findByNomContainingIgnoreCase(String nomPartiel);

	@Query("select c from Client c order by c.nom asc")
	List<Client> listClientsOrderByNom();
}
