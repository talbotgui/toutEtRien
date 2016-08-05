package com.github.talbotgui.minibootrestjpa.metier.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.github.talbotgui.minibootrestjpa.metier.entities.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

	@Query("select u from Utilisateur u order by u.nom")
	Collection<Utilisateur> findUtilisateurOrderByNom();

}
