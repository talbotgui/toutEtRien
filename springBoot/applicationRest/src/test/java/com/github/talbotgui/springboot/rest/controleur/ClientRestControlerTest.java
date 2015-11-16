package com.github.talbotgui.springboot.rest.controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.googlecode.catchexception.CatchException;
import com.github.talbotgui.springboot.business.entities.Client;
import com.github.talbotgui.springboot.rest.application.RestApplication;
import com.github.talbotgui.springboot.rest.controleur.dto.PageDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientRestControlerTest {

	private static final String URL = "http://localhost:8080";

	@AfterClass
	public static void afterClass() {
		RestApplication.stop();
	}

	@BeforeClass
	public static void beforeClass() {
		RestApplication.start();
	}

	private final Log LOG = LogFactory.getLog(ClientRestControlerTest.class);

	private RestTemplate rest;

	@Before
	public void before() {
		this.rest = new RestTemplate();

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new ClientHttpRequestInterceptor() {

			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				LOG.info("Request : {URI=" + request.getURI() + ", method=" + request.getMethod().name() + ", headers="
						+ request.getHeaders() + ", body=" + new String(body) + "}");
				ClientHttpResponse response = execution.execute(request, body);
				LOG.info("Response : {code=" + response.getStatusCode() + "}");
				return response;
			}
		});
		rest.setInterceptors(interceptors);
	}

	@Test
	public void test01Count_0() {

		// ACT
		Long count = rest.getForObject(URL + "/client/count", Long.class);

		// ASSERT
		Assert.assertEquals(Long.valueOf(0), count);
	}

	@Test
	public void test02Creer_ok() {
		final String prenom = "Guillaume";
		final String nom = "Tel";

		// ARRANGE
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("prenom", prenom);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Client client = rest.postForObject(URL + "/client", requestParam, Client.class, uriVars);

		// ASSERT
		Assert.assertNotNull(client);
		Assert.assertEquals(prenom, client.getPrenom());
		Assert.assertEquals(nom, client.getNom());
	}

	@Test
	public void test03Count_1() {

		// ACT
		Long count = rest.getForObject(URL + "/client/count", Long.class);

		// ASSERT
		Assert.assertEquals(Long.valueOf(1), count);
	}

	@Test
	public void test04Creer_ko() {
		final String nom = "Maradona";
		final String prenom = "Diego";

		// ARRANGE
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("prenom", prenom);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Client client = rest.postForObject(URL + "/client", requestParam, Client.class, uriVars);
		CatchException.catchException(rest).postForObject(URL + "/client", requestParam, Client.class, uriVars);

		// ASSERT
		Assert.assertNotNull(client);
		Assert.assertEquals(prenom, client.getPrenom());
		Assert.assertEquals(nom, client.getNom());
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertEquals(HttpClientErrorException.class, CatchException.caughtException().getClass());
		HttpStatusCodeException e = (HttpStatusCodeException) CatchException.caughtException();
		Assert.assertEquals(409, e.getStatusCode().value());
		Assert.assertEquals("CLIENT_NOM_DEJA_EXISTANT-Un client portant ce nom existe deja (nom=" + nom + ", prenom="
				+ prenom + ").", e.getResponseBodyAsString());
	}

	@Test
	public void test05ListClientsOrderByNomPagine() {

		// ******* ARRANGE : 2 personnes déjà créées et on crée 10 moutons
		for (int i = 1; i <= 10; i++) {
			MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
			requestParam.add("nom", "Mouton");
			requestParam.add("prenom", "n" + i);
			rest.postForObject(URL + "/client", requestParam, Client.class, new HashMap<String, Object>());
		}

		// ******* ACT :
		// création du type de retour
		ParameterizedTypeReference<PageDTO<Client>> typeRetour = new ParameterizedTypeReference<PageDTO<Client>>() {
		};

		// creation de la map avec le resultat des appels pour les pages de 0 à 6 (la derniere page sera vide car
		// seulement 12 elements existent en base)
		Map<Integer, ResponseEntity<PageDTO<Client>>> pages = new HashMap<Integer, ResponseEntity<PageDTO<Client>>>();
		for (int i = 0; i <= 6; i++) {
			Map<String, Object> uriVars = new HashMap<String, Object>();
			uriVars.put("size", 2L);
			uriVars.put("index", i);
			pages.put(i, rest.exchange(URL + "/client/{size}/{index}", HttpMethod.GET, null, typeRetour, uriVars));
		}

		// ******* ASSERT
		for (int i = 0; i <= 6; i++) {
			Assert.assertNotNull(pages.get(i));
			Assert.assertEquals(12, pages.get(0).getBody().getTotalElements());
			// Les 5 premieres pages contiennent des données et pas la derniere
			if (i < 6) {
				Assert.assertEquals(2, pages.get(i).getBody().getNumberOfElements());
			} else {
				Assert.assertEquals(0, pages.get(i).getBody().getNumberOfElements());
			}
		}
		Assert.assertEquals("Guillaume", pages.get(0).getBody().getContent().get(0).getPrenom());
		Assert.assertEquals("Diego", pages.get(0).getBody().getContent().get(1).getPrenom());
		Assert.assertEquals("n1", pages.get(1).getBody().getContent().get(0).getPrenom());
		Assert.assertEquals("n3", pages.get(2).getBody().getContent().get(0).getPrenom());
		Assert.assertEquals("n5", pages.get(3).getBody().getContent().get(0).getPrenom());
		Assert.assertEquals("n7", pages.get(4).getBody().getContent().get(0).getPrenom());
		Assert.assertEquals("n9", pages.get(5).getBody().getContent().get(0).getPrenom());
	}

	@Test
	public void test06ListClientsOrderByNom() {
		// création du type de retour
		ParameterizedTypeReference<List<Client>> typeRetour = new ParameterizedTypeReference<List<Client>>() {
		};

		// Appel
		ResponseEntity<List<Client>> clients = rest.exchange(URL + "/client", HttpMethod.GET, null, typeRetour,
				new HashMap<String, Object>());

		Assert.assertNotNull(clients);
		Assert.assertEquals(12, clients.getBody().size());
		Assert.assertEquals(Client.class, clients.getBody().get(0).getClass());
	}

	@Test
	public void test07CreationAvecTransaction() {

		// ARRANGE
		// création du type de retour
		ParameterizedTypeReference<List<Client>> typeRetour = new ParameterizedTypeReference<List<Client>>() {
		};
		// recuperation du nombre de client avant le test
		Long nbAvantCreation = rest.getForObject(URL + "/client/count", Long.class);

		// ACT
		ArrayList<Client> clients = new ArrayList<>();
		clients.add(new Client("The", "Jack"));
		clients.add(new Client("The", "John"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ArrayList<Client>> entity = new HttpEntity<ArrayList<Client>>(clients, headers);

		ResponseEntity<List<Client>> response = rest.exchange(URL + "/clients", HttpMethod.POST, entity, typeRetour);

		// ASSERT
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(2, response.getBody().size());
		Assert.assertEquals("The", response.getBody().get(0).getNom());
		Assert.assertNotNull(response.getBody().get(0).getId());

		Assert.assertEquals(Long.valueOf(nbAvantCreation + 2L), rest.getForObject(URL + "/client/count", Long.class));
	}

	@Test
	public void test08CreationEtErreurAvecTransaction() {

		// ARRANGE
		Long nbAvantCreation = rest.getForObject(URL + "/client/count", Long.class);

		// ACT
		ArrayList<Client> clients = new ArrayList<>();
		clients.add(new Client("Theroude", "Jack"));
		clients.add(new Client("Theroude", "John"));
		clients.add(new Client("Theroude", "Jack"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ArrayList<Client>> entity = new HttpEntity<ArrayList<Client>>(clients, headers);

		CatchException.catchException(rest).postForEntity(URL + "/clients", entity, List.class);

		// ASSERT
		Assert.assertNotNull("Aucune exception declenchee", CatchException.caughtException());
		Assert.assertEquals(HttpClientErrorException.class, CatchException.caughtException().getClass());
		HttpStatusCodeException e = (HttpStatusCodeException) CatchException.caughtException();
		Assert.assertEquals(409, e.getStatusCode().value());
		Assert.assertEquals("CLIENT_NOM_DEJA_EXISTANT-Un client portant ce nom existe deja (nom=" + "Theroude"
				+ ", prenom=" + "Jack" + ").", e.getResponseBodyAsString());

		Assert.assertEquals(nbAvantCreation, rest.getForObject(URL + "/client/count", Long.class));
	}

	@Test
	public void test09CreationEtErreurSansTransaction() {

		// ARRANGE
		Long nbAvantCreation = rest.getForObject(URL + "/client/count", Long.class);

		// ACT
		ArrayList<Client> clients = new ArrayList<>();
		clients.add(new Client("DupontMartin", "Jack"));
		clients.add(new Client("DupontMartin", "John"));
		clients.add(new Client("DupontMartin", "Jack"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ArrayList<Client>> entity = new HttpEntity<ArrayList<Client>>(clients, headers);

		CatchException.catchException(rest).postForEntity(URL + "/clientsNoT", entity, List.class);

		// ASSERT
		Assert.assertNotNull("Aucune exception declenchee", CatchException.caughtException());
		Assert.assertEquals(HttpClientErrorException.class, CatchException.caughtException().getClass());
		HttpStatusCodeException e = (HttpStatusCodeException) CatchException.caughtException();
		Assert.assertEquals(409, e.getStatusCode().value());
		Assert.assertEquals("CLIENT_NOM_DEJA_EXISTANT-Un client portant ce nom existe deja (nom=" + "DupontMartin"
				+ ", prenom=" + "Jack" + ").", e.getResponseBodyAsString());

		Assert.assertEquals(Long.valueOf(nbAvantCreation + 2L), rest.getForObject(URL + "/client/count", Long.class));
	}

}
