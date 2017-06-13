package com.company;

import com.company.cards.CardCollection;
import com.company.cards.Hand;
import com.company.cards.tooFewCardsInCollectionException;

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
		hands.add(new Hand());
	}

	/**
	 * Remove all hands that were created from a split
	 */
	void clearHands() {
		hands.clear();
	}


	/**
	 * Given the deck, and the original hand that the player will split with:
	 * 1. Create a new hand for the player
	 * 2. Give the new hand one of the cards from the original hand
	 * 3. Distribute a new card to each of the hands from the deck.
	 * @param deck The deck to draw cards from after the split occurs
	 * @param originalHand The hand to split with
	 */
	void split(CardCollection deck, Hand originalHand) throws tooFewCardsInCollectionException {
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
		Hand newHand = hands.get(hands.size() - 1);

		// Transfer one of the cards to the new hand...
		newHand.draw(originalHand, 1);

		// .. And have each hand draw a new card
		originalHand.draw(deck, 1);
		newHand.draw(deck, 1);
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
	private ArrayList<Hand> hands;
}
