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

	@Test
	void split_correctNumberOfCardsInHand() {
		try {
			// Creating a fake deck, which will distribute cards that can be split
			Hand pseudoDeck = new Hand();

			Card c = new Card();
			c.setRank(Card.Rank.TEN);
			c.setSuit(Card.Suit.HEART);
			pseudoDeck.push(c);

			c = new Card();
			c.setRank(Card.Rank.JACK);
			c.setSuit(Card.Suit.SPADE);
			pseudoDeck.push(c);


			// Drawing
			p.addHand();
			p.getHand().draw(pseudoDeck, 2);

			// Perform the split
			p.split(d, p.getHand());


			// Test the number of cards in each deck
			int cardNums_deckA = p.getAllHands().get(0).getNumCards();
			int cardNums_deckB = p.getAllHands().get(1).getNumCards();

			assertEquals(cardNums_deckA, 2);
			assertEquals(cardNums_deckB, 2);

		} catch (tooFewCardsInCollectionException e) {
			// Something went wrong; Could not split due to card counts
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	void split_correctNumberOfHands() {
		try {
			// Creating a fake deck, which will distribute cards that can be split
			Hand pseudoDeck = new Hand();

			// We will be creating 3 "splittable" cards, and 1 "non-splittable" card
			// The end result will be an opening hand of [10H, JH]
			// Which will split into [10H, QH], [JH, AH]
			// Which will split into [10H, 2H], [QH, 3H], [JH, AH]

			// A grand total of 3 splits, resulting in 3 hands.
			Card c;

			// Non-splittable cards
			c = new Card();
			c.setSuit(Card.Suit.HEART);
			c.setRank(Card.Rank.THREE);
			pseudoDeck.push(c);
			c = new Card();
			c.setSuit(Card.Suit.HEART);
			c.setRank(Card.Rank.TWO);
			pseudoDeck.push(c);
			c = new Card();
			c.setSuit(Card.Suit.HEART);
			c.setRank(Card.Rank.ACE);
			pseudoDeck.push(c);

			// Creating splittable cards
			c = new Card();
			c.setSuit(Card.Suit.HEART);
			c.setRank(Card.Rank.QUEEN);
			pseudoDeck.push(c);
			c = new Card();
			c.setSuit(Card.Suit.HEART);
			c.setRank(Card.Rank.JACK);
			pseudoDeck.push(c);
			c = new Card();
			c.setSuit(Card.Suit.HEART);
			c.setRank(Card.Rank.TEN);
			pseudoDeck.push(c);


			// Drawing
			p.addHand();
			p.getHand().draw(pseudoDeck, 2);

			// Iterating through each hand to see if we can split
			int handNum;
			for(handNum = 0; handNum < p.getNumHands(); handNum++) {
				Hand currHand = p.getHand(handNum);

				// Perfom splits on all hands
				if(currHand.canSplit()) {
					p.split(pseudoDeck, currHand);
					handNum--;
				}
			}

			// Test the number of hands we've ended up with
			assertEquals(p.getNumHands(), 3);

		} catch (tooFewCardsInCollectionException e) {
			// Something went wrong; Could not split due to card counts
			e.printStackTrace();
			assertTrue(false);
		}
	}
}