package com.github.talbotgui.springboot.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.springboot.business.entities.Client;

public interface ClientService {

	long countClients();

	Client creerClient(String nom, String prenom);

	Collection<Client> creerClients(Collection<Client> clientsAcreer);

	Collection<Client> creerClientsSansTransaction(Collection<Client> clientsAcreer);

	public Page<Client> findAllByPage(Pageable pageable);

	List<Client> findByNomAndPrenom(String nom, String prenom);

	List<Client> findByNomContainingIgnoreCase(String nomPartiel);

	List<Client> listClientsOrderByNom();
}
