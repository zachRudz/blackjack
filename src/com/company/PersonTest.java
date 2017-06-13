package com.company;

import com.company.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by zach on 12/06/17.
 */
class PersonTest {
	Deck d;
	Person p;

	@BeforeEach
	void setUp() {
		d = new Deck();
		p = new HumanPlayer("Testplayer", 500.00);
	}

	@Test
	void clearHands() {
		d = new Deck();
		p = new HumanPlayer("Testplayer", 500.00);

		// Create hands
		p.addHand();
		p.addHand();
		p.addHand();

		// Populate some of them
		p.getHand().draw(d, 2);
		p.getAllHands().get(1).draw(d,3);

		// Trashing them all
		p.clearHands();

		// Make sure that clearHands() properly destroys all of the hands
		assertEquals(p.getAllHands().size(), 0);
	}

}