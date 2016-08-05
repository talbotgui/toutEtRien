package com.github.talbotgui.minibootrestjpa.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.minibootrestjpa.metier.entities.Utilisateur;
import com.github.talbotgui.minibootrestjpa.metier.service.MonService;

@RestController
public class UtilisateurRestControler {

	@Autowired
	private MonService monService;

	@RequestMapping(value = "/utilisateur", method = GET)
	public Collection<Utilisateur> listeUtilisateur() {
		return this.monService.listeUtilisateurs();
	}

	@RequestMapping(value = "/utilisateur", method = POST)
	public void sauvegardeUtilisateur(//
			@RequestParam(value = "nom") final String nom, //
			@RequestParam(value = "prenom") final String prenom) {
		this.monService.creeUtilisateur(new Utilisateur(nom, prenom));
	}
}
