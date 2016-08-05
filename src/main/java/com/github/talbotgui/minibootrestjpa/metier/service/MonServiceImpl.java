package com.github.talbotgui.minibootrestjpa.metier.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.talbotgui.minibootrestjpa.metier.dao.UtilisateurRepository;
import com.github.talbotgui.minibootrestjpa.metier.entities.Utilisateur;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MonServiceImpl implements MonService {

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Override
	public Long creeUtilisateur(final Utilisateur utilisateur) {
		return this.utilisateurRepository.save(utilisateur).getId();
	}

	@Override
	public Collection<Utilisateur> listeUtilisateurs() {
		return this.utilisateurRepository.findUtilisateurOrderByNom();
	}

}
