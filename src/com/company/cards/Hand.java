package com.company.cards;

import com.company.Player;

/**
 * The hand that is used by either a player or a dealer.
 * Some operations can only be used by a player (betting), since the dealer would never have a use for a betting mechanism
 * Created by zach on 20/05/17.
 */
public class Hand extends CardCollection {
	//region Constructor
	//==================================================================================================================
	private Hand() {};
	public Hand(Player owner) {
		super();
		maxNumCards = 7;
		cards = new Card[maxNumCards];
		status = null;
		this.owner = owner;
	}
	//endregion


	//region Cards
	//==================================================================================================================

	/**
	 * Move $numCardsToDraw from the source deck to the hand.
	 * @param source Source of cards to draw
	 * @param numCardsToDraw How many cards to draw
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
	 * @return The total rank of the hand
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
	//endregion


	//region Splitting
	//==================================================================================================================
	/**
	 * Given our starting hand, evaluate whether or not we can split.
	 * This means that our starting 2 cards must have the same rank (ie: Jack of hearts, 10 of diamonds)
	 * @return Returns true or false depending on if we can split with this hand
	 */
	public Boolean canSplit() {
		return (getNumCards() == 2) && cards[0].getRankValue() == cards[1].getRankValue();

	}
	//endregion


	//region Statuses
	//==================================================================================================================
	public String getStatus() {
		return status.toString();
	}

	/**
	 * Evalutates a player's hand, to see how it faired.
	 */
	public void evaluateStatus() {
		int rank = getTotalRank();

		if(rank == 21 && getNumCards() == 2) {
			// Natural 21
			status = Status.natural;
		} else if(rank > 21) {
			// Busted out
			status = Status.busted;
		} else {
			// Player is safe
			status = Status.safe;
		}
	}
	//endregion

	//region Bets
	//==================================================================================================================
	// The player's bet for the k'th round
	public void setBet(double bet) {
		if(owner == null) {
			System.err.println("Error: The dealer cannot double down.");
			return;
		}

		if(bet > owner.getFunds()) {
			throw new ArithmeticException("Not enough funds to bet that much!");
		}

		this.bet = bet;
		owner.removeFunds(bet);
	}

	public double getBet() { return bet; }

	public void doubleDown() {
		if(owner == null) {
			System.err.println("Error: The dealer cannot double down.");
			return;
		}

		// Throw an exception if we can't double down
		if(bet > owner.getFunds()) {
			throw new ArithmeticException("Not enough funds to double down!");
		}

		// Double the bet on our end
		owner.removeFunds(bet);
		bet *= 2;
	}
	//endregion

	// After the player has played their round, what is their ending status?
	// Did they bust out?
	// Did they choose to stand?
	// Did they hit a natural (total rank of 21 on the first 2 cards)
	private enum Status {
		busted, safe, natural
	};


	private Status status;
	private double bet;

	/*
	In the case of the dealer, this will be null. This is because all references to the owner will be in use cases for
	accessing the owner's bet. The dealer has no bet, and so the bet functions are not defined here.
	 */
	private Player owner;
}
