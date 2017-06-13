package com.company.cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by zach on 12/06/17.
 */
class HandTest {
	@Test
	void canSplit_DifferentFaceValues() {
		Hand h = new Hand();

		// Creating cards of different suits and face values
		Card c = new Card();
		c.setRank(Card.Rank.JACK);
		c.setSuit(Card.Suit.CLUB);
		h.push(c);

		c = new Card();
		c.setRank(Card.Rank.TEN);
		c.setSuit(Card.Suit.SPADE);
		h.push(c);

		assertTrue(h.canSplit());
	}

	@Test
	void canSplit_DifferentRanks() {
		Hand h = new Hand();

		// Creating cards of different suits and face values
		Card c = new Card();
		c.setRank(Card.Rank.NINE);
		c.setSuit(Card.Suit.CLUB);
		h.push(c);

		c = new Card();
		c.setRank(Card.Rank.TEN);
		c.setSuit(Card.Suit.SPADE);
		h.push(c);

		assertFalse(h.canSplit());
	}
}