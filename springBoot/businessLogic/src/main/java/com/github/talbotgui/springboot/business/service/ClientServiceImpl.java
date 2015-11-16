package com.github.talbotgui.springboot.business.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.talbotgui.springboot.business.dao.ClientPaginateRepository;
import com.github.talbotgui.springboot.business.dao.ClientRepository;
import com.github.talbotgui.springboot.business.entities.Client;
import com.github.talbotgui.springboot.business.exception.BusinessException;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientPaginateRepository clientPaginateRepository;

	@Autowired
	private ClientRepository clientRepository;

	public long countClients() {
		return this.clientRepository.count();
	}

	public Client creerClient(String nom, String prenom) {

		// Controle des données
		if (StringUtils.isEmpty(nom) || StringUtils.isEmpty(prenom)) {
			throw new BusinessException(BusinessException.CLIENT_NOM_ET_PRENOM_OBLIGATOIRES,
					new String[] { nom, prenom });
		}

		// Controle de pre-existance
		List<Client> clientsPreExistant = this.clientRepository.findByNomAndPrenom(nom, prenom);
		if (clientsPreExistant != null && !clientsPreExistant.isEmpty()) {
			throw new BusinessException(BusinessException.CLIENT_NOM_DEJA_EXISTANT, new String[] { nom, prenom });
		}

		// Creation
		return this.clientRepository.save(new Client(nom, prenom));

	}

	@Override
	public Collection<Client> creerClients(Collection<Client> clientsAcreer) {
		Collection<Client> res = new ArrayList<>();
		for (Client c : clientsAcreer) {
			res.add(this.creerClient(c.getNom(), c.getPrenom()));
		}
		return res;
	}

	@Override
	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	public Collection<Client> creerClientsSansTransaction(Collection<Client> clientsAcreer) {
		Collection<Client> res = new ArrayList<>();
		for (Client c : clientsAcreer) {
			res.add(this.creerClient(c.getNom(), c.getPrenom()));
		}
		return res;
	}

	public Page<Client> findAllByPage(Pageable pageable) {
		return clientPaginateRepository.findAll(pageable);
	}

	public List<Client> findByNomAndPrenom(String nom, String prenom) {
		return this.clientRepository.findByNomAndPrenom(nom, prenom);
	}

	public List<Client> findByNomContainingIgnoreCase(String nomPartiel) {
		return this.clientRepository.findByNomContainingIgnoreCase(nomPartiel);
	}

	public List<Client> listClientsOrderByNom() {
		return this.clientRepository.listClientsOrderByNom();
	}

}
