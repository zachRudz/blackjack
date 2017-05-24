package com.company;

import com.company.cards.Deck;
import com.company.cards.Hand;

/**
 * A person playing the game. This is either a human player, CPU player, or the dealer
 * Created by zach on 23/05/17.
 */
public class Person {
	Person() {
		hand = new Hand();
	}
	
	/*********************
	 * Getters and setters
	 */
	// Hand
	public Hand getHand() {
		return hand;
	}
	
	public void printCards() {
		hand.printCards();
	}
	
	public void draw(Deck source, int numCardsToDraw) {
		hand.draw(source, numCardsToDraw);
	}
	
	/**********************
	 * Play a round of blackjack. Overwritten by children classes.
	 * Returns true if the player went over 21.
	 * @param dealer
	 * @param otherPlayers
	 * @return didPlayerBust
	 */
	protected Boolean play(Deck deck, Dealer dealer, Player[] otherPlayers) { return true; }
	
	
	private Hand hand;
}
