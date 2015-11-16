package com.github.talbotgui.springboot.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.springboot.business.entities.Client;
import com.github.talbotgui.springboot.business.service.ClientService;
import com.github.talbotgui.springboot.rest.controleur.dto.PageDTO;

@RestController()
public class ClientRestControler {

	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/client/count", method = RequestMethod.GET)
	public long count() {
		return this.clientService.countClients();
	}

	@RequestMapping(value = "/client", method = POST)
	public Client creerClient(@RequestParam(value = "nom") String nom, @RequestParam(value = "prenom") String prenom) {
		return this.clientService.creerClient(nom, prenom);
	}

	@RequestMapping(value = "/clientObject", method = POST)
	public Client creerClientObject(@RequestBody Client client) {
		return this.clientService.creerClient(client.getNom(), client.getPrenom());
	}

	@RequestMapping(value = "/clients", method = POST)
	public ArrayList<Client> creerClients(@RequestBody ArrayList<Client> clientsAcreer) {
		ArrayList<Client> clients = new ArrayList<>();
		clients.addAll(this.clientService.creerClients(clientsAcreer));
		return clients;
	}

	@RequestMapping(value = "/clientsNoT", method = POST)
	public ArrayList<Client> creerClientsSansTransaction(@RequestBody ArrayList<Client> clientsAcreer) {
		ArrayList<Client> clients = new ArrayList<>();
		clients.addAll(this.clientService.creerClientsSansTransaction(clientsAcreer));
		return clients;
	}

	@RequestMapping(value = "/client", method = GET)
	public List<Client> listClientsOrderByNom() {
		return this.clientService.listClientsOrderByNom();
	}

	@RequestMapping(value = "/client/{size}/{index}", method = GET)
	public PageDTO<Client> listClientsOrderByNom(@PathVariable("size") Integer size,
			@PathVariable("index") Integer index) {
		return new PageDTO<Client>(this.clientService.findAllByPage(new PageRequest(index, size)));
	}
}
