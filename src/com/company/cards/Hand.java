package com.company.cards;

/**
 * Created by zach on 20/05/17.
 */
public class Hand extends CardCollection {
	public Hand() {
		maxNumCards = 7;
		cards = new Card[maxNumCards];
	}
	
	public void draw(Deck source, int numCardsToDraw) {
		while(numCardsToDraw > 0 && source.numCards > 0) {
			push(source.pop());
			numCardsToDraw--;
		}

	}
	
	// Sum up the scores of the cards in your hand, according to blackjack rules
	public int getTotalRank() {
		int numAces = 0, totalRank = 0;
		
		// Get total rank value before applying ace modification (Ace value being 1 or 11?)
		for(Card c : cards) {
			// Stop checking cards if we've reached the end of our cards
			if(c == null) break;
			
			if(c.getRank() == Card.Rank.ACE)
				numAces++;
			
			totalRank += c.getRankValue();
		}
		
		// Applying ace modification
		// (Would we be better off if an ace were treated as a 1 or an 11?)
		while(numAces > 0) {
			// If we don't go over 21, then treat the ace as an 11 instead of the default 1.
			if(totalRank + 10 < 21) {
				totalRank += 10;
			}
			numAces--;
		}
		
		return totalRank;
	}
	
	// Print cards in hand, and also the score of the cards in the hand
	public void printCards() {
		System.out.println("Score: [" + getTotalRank() + "]");
		super.printCards();
	}
}
