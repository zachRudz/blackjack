package com.company;

import com.company.cards.CardCollection;
import com.company.cards.Deck;
import com.company.cards.Hand;
import com.company.cards.tooFewCardsInCollectionException;

import java.util.ArrayList;

/**
 * The player at the table. This could be a CPU or a human player.
 * Created by zach on 23/05/17.
 */
public abstract class Player extends Person {
	public Player(String name, double startingFunds) {
		super();
		this.name = name;
		this.funds = startingFunds;
	}
	
	
	
	/********************
	 * Returns a string representation of the player.
	 * @return {name} [${funds}]
	 */
	public String toString() {
		return name + String.format(" [$%.2f]", funds);
	}

	

	//region Name
	//==================================================================================================================
	String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	//endregion
	
	
	//region Funds
	//==================================================================================================================
	public double getFunds() {
		return funds;
	}

	public void setFunds(double funds) {
		this.funds = funds;
	}
	
	public void addFunds(double fundsToAdd) {
		this.funds += fundsToAdd;
	}

	public void removeFunds(double fundsToRemove) {
		funds -= fundsToRemove;
	}
	//endregion


	//region Cards
	//==================================================================================================================
	public void addHand() {
		// Overloading, since the parent class holds onto null as the owner.
		hands.add(new Hand(this));
	}

	/**
	 * Given the deck, and the original hand that the player will split with:
	 * 1. Create a new hand for the player
	 * 2. Give the new hand one of the cards from the original hand
	 * 3. Distribute a new card to each of the hands from the deck.
	 * @param deck The deck to draw cards from after the split occurs
	 * @param originalHand The hand to split with
	 */
	public void split(CardCollection deck, Hand originalHand) throws tooFewCardsInCollectionException {
		// Making sure that the original hand has enough cards
		if(originalHand.getNumCards() != 2) {
			System.err.println("Error: Not enough cards in the original hand");
			throw new tooFewCardsInCollectionException();
		}

		// Also making sure that the deck has enough cards
		if(deck.getNumCards() < 2) {
			System.err.println("Error: Not enough cards in the deck to draw (numCards: " + deck.getNumCards() + ")");
			throw new tooFewCardsInCollectionException();
		}

		// Create the new hand
		addHand();
		Hand newHand = getAllHands().get(getNumHands() - 1);

		// Put down a new betting amount on the new bet
		double originalBet = originalHand.getBet();
		newHand.setBet(originalBet);

		// Transfer one of the cards to the new hand...
		newHand.draw(originalHand, 1);

		// .. And have each hand draw a new card
		originalHand.draw(deck, 1);
		newHand.draw(deck, 1);
	}
	//endregion


	//region Abstract Game Functions
	//==================================================================================================================
	public abstract void placeBet(double minimumBet) throws NotEnoughFundsException;
	public abstract void play(Deck deck, Dealer dealer, ArrayList<Player> otherPlayerHands);
	//endregion
	

	private String name;
	private double funds;
}
