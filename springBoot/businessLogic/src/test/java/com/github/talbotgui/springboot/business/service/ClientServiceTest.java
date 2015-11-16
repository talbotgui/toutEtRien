package com.github.talbotgui.springboot.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.catchexception.CatchException;
import com.github.talbotgui.springboot.business.entities.Client;
import com.github.talbotgui.springboot.business.exception.BusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientServiceTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ClientService instance;

	@Test
	public void test00Count0() {

		// ARRANGE
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("delete from T_CLIENT");

		// ACT
		long count = instance.countClients();

		// ASSERT
		Assert.assertEquals(0, count);
	}

	@Test
	public void test01Count3() {

		// ARRANGE
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(
				new ClassPathResource("sql/dataClientServiceTest_nom.sql"));
		databasePopulator.execute(dataSource);

		// ACT
		long count = instance.countClients();

		// ASSERT
		Assert.assertEquals(3, count);
	}

	@Test
	public void test02CreerClientBaseVide() {

		// ARRANGE
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("delete from T_CLIENT");

		// ACT
		final String nomClient = "Theroude";
		final String prenomClient = "Jack";
		Client client = instance.creerClient(nomClient, prenomClient);
		List<Client> listeClients = instance.listClientsOrderByNom();

		// ASSERT
		Assert.assertNotNull(client);
		Assert.assertEquals(nomClient, client.getNom());
		Assert.assertEquals(prenomClient, client.getPrenom());
		Assert.assertNotNull(client.getId());
		Assert.assertNotNull(listeClients);
		Assert.assertEquals(1, listeClients.size());
	}

	@Test
	public void test03CreerClientNomsIdentiques() {

		// ARRANGE
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("delete from T_CLIENT");

		// ACT
		final String nomClient = "Theroude";
		final String prenomClient1 = "Jack";
		final String prenomClient2 = "John";
		Client client1 = instance.creerClient(nomClient, prenomClient1);
		Client client2 = instance.creerClient(nomClient, prenomClient2);
		CatchException.catchException(instance).creerClient(nomClient, prenomClient1);

		// ASSERT
		Assert.assertNotNull(client1);
		Assert.assertEquals(nomClient, client1.getNom());
		Assert.assertEquals(prenomClient1, client1.getPrenom());
		Assert.assertNotNull(client1.getId());
		Assert.assertNotNull(client2);
		Assert.assertEquals(nomClient, client2.getNom());
		Assert.assertEquals(prenomClient2, client2.getPrenom());
		Assert.assertNotNull(client2.getId());
		Exception e = CatchException.caughtException();
		Assert.assertNotNull("Aucune exception declenchee", e);
		Assert.assertEquals(BusinessException.class, e.getClass());
		Assert.assertEquals(BusinessException.CLIENT_NOM_DEJA_EXISTANT, ((BusinessException) e).getExceptionId());
	}

	@Test
	public void test04RechercheParNomOuPrenom() {

		// ARRANGE
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(
				new ClassPathResource("sql/dataClientServiceTest_nomEtPrenom.sql"));
		databasePopulator.execute(dataSource);

		// ACT
		List<Client> clientsFARBY = instance.findByNomAndPrenom("FARBY", null);
		List<Client> clientsDUPOND = instance.findByNomAndPrenom("DUPOND", null);
		List<Client> clientsDUPONT = instance.findByNomAndPrenom("DUPONT", null);
		List<Client> clientsTOTO = instance.findByNomAndPrenom("TOTO", null);
		List<Client> clientsJean = instance.findByNomAndPrenom(null, "Jean");
		List<Client> clientsMarcel = instance.findByNomAndPrenom(null, "Marcel");
		List<Client> clientsTiti = instance.findByNomAndPrenom(null, "TITI");
		List<Client> clientsFarbyMarcel = instance.findByNomAndPrenom("FARBY", "Marcel");
		List<Client> clientsFarbyAlphonse = instance.findByNomAndPrenom("FARBY", "Alphonse");

		// ASSERT
		Assert.assertNotNull(clientsFARBY);
		Assert.assertEquals("Nom en double", 2, clientsFARBY.size());
		Assert.assertNotNull(clientsDUPOND);
		Assert.assertEquals("Nom un seul", 1, clientsDUPOND.size());
		Assert.assertNotNull(clientsDUPONT);
		Assert.assertEquals("Nom en double", 2, clientsDUPONT.size());
		Assert.assertNotNull(clientsTOTO);
		Assert.assertEquals("Nom aucun", 0, clientsTOTO.size());
		Assert.assertNotNull(clientsJean);
		Assert.assertEquals("Prenom en double", 2, clientsJean.size());
		Assert.assertNotNull(clientsMarcel);
		Assert.assertEquals("Prenom un seul", 1, clientsMarcel.size());
		Assert.assertNotNull(clientsTiti);
		Assert.assertEquals("Prenom aucun", 0, clientsTiti.size());
		Assert.assertNotNull(clientsFarbyMarcel);
		Assert.assertEquals("Nom et prenom un seul", 1, clientsFarbyMarcel.size());
		Assert.assertNotNull(clientsFarbyAlphonse);
		Assert.assertEquals("Nom et prenom aucun", 0, clientsFarbyAlphonse.size());

	}

	@Test
	public void test05RecherchePaginee() {

		// ARRANGE
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(
				new ClassPathResource("sql/dataClientServiceTest_nomEtPrenom.sql"));
		databasePopulator.execute(dataSource);

		// ACT
		Page<Client> page1 = instance.findAllByPage(new PageRequest(0, 2));
		Page<Client> page2 = instance.findAllByPage(page1.nextPageable());
		Page<Client> page3 = instance.findAllByPage(page2.nextPageable());

		// ASSERT
		Assert.assertNotNull("Page 1", page1);
		Assert.assertEquals("Page 1", 2, page1.getNumberOfElements());
		Assert.assertEquals("Page 1", 5, page1.getTotalElements());
		Assert.assertEquals("Page 1", "FARBY", page1.getContent().get(0).getNom());
		Assert.assertEquals("Page 1", "FARBY", page1.getContent().get(1).getNom());
		Assert.assertNotNull("Page 2", page2);
		Assert.assertEquals("Page 2", 2, page2.getNumberOfElements());
		Assert.assertEquals("Page 2", 5, page2.getTotalElements());
		Assert.assertEquals("Page 2", "DUPONT", page2.getContent().get(0).getNom());
		Assert.assertEquals("Page 2", "DUPONT", page2.getContent().get(1).getNom());
		Assert.assertNotNull("Page 3", page3);
		Assert.assertEquals("Page 3", 1, page3.getNumberOfElements());
		Assert.assertEquals("Page 3", 5, page3.getTotalElements());
		Assert.assertEquals("Page 3", "DUPOND", page3.getContent().get(0).getNom());
	}

	@Test
	public void test06CreationAvecTransaction() {

		// ARRANGE
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("delete from T_CLIENT");

		// ACT
		List<Client> clients = new ArrayList<>();
		clients.add(new Client("Theroude", "Jack"));
		clients.add(new Client("Theroude", "John"));
		clients.add(new Client("Theroude", "Jack"));
		CatchException.catchException(instance).creerClients(clients);

		// ASSERT
		Exception e = CatchException.caughtException();
		Assert.assertNotNull("Aucune exception declenchee", e);
		Assert.assertEquals(BusinessException.class, e.getClass());
		Assert.assertEquals(BusinessException.CLIENT_NOM_DEJA_EXISTANT, ((BusinessException) e).getExceptionId());
		Assert.assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from T_CLIENT", Long.class));
	}

	@Test
	public void test07CreationSansTransaction() {

		// ARRANGE
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.update("delete from T_CLIENT");

		// ACT
		List<Client> clients = new ArrayList<>();
		clients.add(new Client("Theroude", "Jack"));
		clients.add(new Client("Theroude", "John"));
		clients.add(new Client("Theroude", "Jack"));
		CatchException.catchException(instance).creerClientsSansTransaction(clients);

		// ASSERT
		Exception e = CatchException.caughtException();
		Assert.assertNotNull("Aucune exception declenchee", e);
		Assert.assertEquals(BusinessException.class, e.getClass());
		Assert.assertEquals(BusinessException.CLIENT_NOM_DEJA_EXISTANT, ((BusinessException) e).getExceptionId());
		Assert.assertEquals(Long.valueOf(2), jdbc.queryForObject("select count(*) from T_CLIENT", Long.class));
	}

}
