package com.github.talbotgui.minibootrestjpa.metier.service;

import java.util.Collection;

import com.github.talbotgui.minibootrestjpa.metier.entities.Utilisateur;

public interface MonService {

	Long creeUtilisateur(Utilisateur utilisateur);

	Collection<Utilisateur> listeUtilisateurs();
}
