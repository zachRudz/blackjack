package com.company;

import com.company.cards.Hand;

import java.util.ArrayList;

/**
 * A person playing the game. This is either a human player, CPU player, or the dealer
 *
 * Created by zach on 23/05/17.
 */
abstract public class Person {
	//region Constructors
	//==================================================================================================================
	Person() {
		hands = new ArrayList<>();
	}
	//endregion



	//region Hand Operations
	//==================================================================================================================

	/**
	 * Fetch the first hand from the player
	 * @return The first hand of the player
	 */
	Hand getHand() {
		return hands.get(0);
	}

	/**
	 * Fetch the $handNum'th hand from the player
	 * @return The hand at index $handNum
	 */
	Hand getHand(int handNum) {
		return hands.get(handNum);
	}

	ArrayList<Hand> getAllHands() {
		return hands;
	}

	int getNumHands() {
		return hands.size();
	}

	/**
	 * Create a hand
	 */
	void addHand() {
		hands.add(new Hand(null));
	}

	/**
	 * Remove all hands that were created from a split
	 */
	void clearHands() {
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


	/**
 	* Each player would have one hand that represents whatever hand they're dealt at the beginning of the round.
	* If the player were to split, then a new hand would be added to the arraylist. At the end of the round, it would be
 	* deleted.
	*/
	ArrayList<Hand> hands;
}
