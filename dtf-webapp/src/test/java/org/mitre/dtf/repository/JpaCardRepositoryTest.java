package org.mitre.dtf.repository;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.dtf.model.Card;
import org.mitre.dtf.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Sets;

/**
 * Unit testing of JpaCardRepository class. These tests makes some assumptions
 * about the initial state of the db as scripted in the cards.sql resource file.
 * 
 * @author wkim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/application-context.xml")
public class JpaCardRepositoryTest {

	@Autowired
	JpaCardRepository cardRepository;

	/*
	 * See src/resources/db/*.sql files for expected initial data.
	 */
	
	// test data
	private Card card1;
	private Card card2;
	
	private Tag tag1;
	
	@Before
	public void setUp() {
		tag1 = new Tag("OP");
		tag1.setId(1L);
		
		card1 = new Card("OpenID Connect", "OpenID Connect 1.0 is a simple identity layer on top of the OAuth 2.0 protocol.");
		card1.setId(1L);
		card1.setDependsTags(Sets.newHashSet(tag1));
		
		card2 = new Card("OpenID Provider", "OAuth 2.0 Authorization Server that is capable of Authenticating the End-User and providing Claims to a Relying Party about the Authentication event and the End-User.");
		card2.setId(2L);
		card2.setProvidesTags(Sets.newHashSet(tag1));
	}
	
	@Test
	public void testInitialState() {

		Set<Card> cards = cardRepository.getAll();
		
		assertTrue(cards.size() == 2);
		assertTrue(cards.contains(card1));
		assertTrue(cards.contains(card2));
	}
	
	@Test
	public void testRoundTrip() {
		
		Card testCard = new Card("id.mitre.org", "MITREid is an OpenID Identity Provider for MITRE employees.");
		
		cardRepository.save(testCard);
		Set<Card> cards = cardRepository.getAll();
		
		testCard.setId(3L); // card in db will have been autogenerated an id of 3
		
		assertTrue(cards.size() == 3);
		assertTrue(cards.contains(testCard));
	}
	
	@Test
	public void testDepends() {
		
		Card c = cardRepository.getById(1); // should be the goldfish card with one depends tag
		Set<Tag> tags = c.getDependsTags();
		
		assertTrue(tags.size() == 1);
		assertTrue(tags.contains(tag1));
	}
	
	@Test
	public void testProvides() {
		
		Card c = cardRepository.getById(2); // should be the fish flakes card with one depends tag
		Set<Tag> tags = c.getProvidesTags();
		
		assertTrue(tags.size() == 1);
		assertTrue(tags.contains(tag1));
	}
	
}