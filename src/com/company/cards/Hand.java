package com.company.cards;

/**
 * Created by zach on 20/05/17.
 */
public class Hand extends CardCollection {
	//region Constructor
	//==================================================================================================================
	public Hand() {
		super();
		maxNumCards = 7;
		cards = new Card[maxNumCards];
	}
	//endregion


	//region Cards
	//==================================================================================================================

	/**
	 * Move $numCardsToDraw from the source deck to the hand.
	 * @param source
	 * @param numCardsToDraw
	 */
	public void draw(CardCollection source, int numCardsToDraw) {
		while(numCardsToDraw > 0 && source.numCards > 0) {
			push(source.pop());
			numCardsToDraw--;
		}

	}


	/**
	 * Print cards in hand, and also the score of the cards in the hand
	 */
	public void printCards() {
		System.out.println("Score: [" + getTotalRank() + "]");
		super.printCards();
	}
	//endregion


	//region Rank
	//==================================================================================================================
	/**
	 * Sum up the scores of the cards in your hand, according to blackjack rules
	 * @return
	 */
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
			if((totalRank + 10) < 22) {
				totalRank += 10;

				}
			numAces--;
		}
		
		return totalRank;
	}

	/**
	 * Given our starting hand, evaluate whether or not we can split.
	 * This means that our starting 2 cards must have the same rank (ie: Jack of hearts, 10 of diamonds)
	 * @return Returns true or false depending on if we can split with this hand
	 */
	public Boolean canSplit() {
		if(getNumCards() == 2)
			return cards[0].getRankValue() == cards[1].getRankValue();

		return false;
	}
	//endregion
}
