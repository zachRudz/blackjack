package com.company;

import com.company.cards.Deck;
import com.company.cards.Hand;

import java.util.ArrayList;

/**
 * A person playing the game. This is either a human player, CPU player, or the dealer
 *
 *
 * Created by zach on 23/05/17.
 */
public class Person {
	//region Constructors
	//==================================================================================================================
	Person() {
		hands = new ArrayList();
	}
	//endregion



	//region Hand Operations
	//==================================================================================================================
	/**
	 *
	 * @return
	 */
	public Hand getHand() {
		return hands.get(0);
	}

	/**
	 * Create a hand
	 * @return handID of the new hand
	 */
	public void addHand() {
		hands.add(new Hand());
	}

	/**
	 * Remove all hands that were created from a split
	 */
	public void clearHands() {
		hands.clear();
	}



	/**
	 * Print all of the cards in each of the player's hands.
	 */
	public void printCards() {
		for(Hand h : hands){
			h.printCards();
		}
	}



	//endregion

	/**********************
	 * Play a round of blackjack.
	 * Returns true if the player went over 21.
	 * @param dealer
	 * @param otherPlayers
	 * @return didPlayerBust
	 */
	protected Boolean play(Deck deck, Dealer dealer, Player[] otherPlayers) { return true; }
	

	/**
 	* Each player would have one hand that represents whatever hand they're dealt at the beginning of the round.
	* If the player were to split, then a new hand would be added to the arraylist. At the end of the round, it would be
 	* deleted.
	*/
	private ArrayList<Hand> hands;
}
