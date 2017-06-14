package com.company;

import com.company.cards.Deck;
import com.company.cards.Card;
import com.company.cards.Hand;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * The dealer of a game of blackjack.
 * Created by zach on 23/05/17.
 */
public class Dealer extends Person {
	/**
	 * Create the dealer, and his hand.
	 * Since the dealer can never split, their (only) hand will be created here.
	 */
	Dealer() {
		super();
		addHand();
	}


	/**
	 * Prints the dealer's hand, and his score
	 * @return A textual representation of the dealer.
	 */
	public String toString() {
		return "Dealer";
	}

	//region Cards
	//==================================================================================================================
	/**
	 * Peek operation, showing only the first card in the dealer's hand
	 * @return the first card of the dealer's hand
	 */
	Card getFirstCard() {
		return getHand().peek(0);
	}

	/**
	 * Print the dealer's hand to stdout
	 */
	public void printCards() {
		System.out.println(String.format("Score: [%d]", getHand().getTotalRank()));
		getHand().printCards();
	}
	//endregion

	/****
	 * The dealer's turn at a round of blackjack.
	 * Dealer stands on a soft 17.
	 * @param deck
	 * @param dealer
	 * @param otherPlayers
	 * @return
	 */
	protected Boolean play(Deck deck, Dealer dealer, ArrayList<Player> otherPlayers) {
		System.out.println();

		int totalRank, highestValidRank = 0;

		// Search through all of the other players, and see who has the highest (valid) hand
		for(Player p : otherPlayers) {
			// Search through all of the players' hands (in the event of a split)
			for(Hand h : p.getAllHands()) {
				// See if this player has a higher rank than the previous highest
				int currentPlayerHandRank = h.getTotalRank();

				if (currentPlayerHandRank < 22 && currentPlayerHandRank > highestValidRank) {
					highestValidRank = currentPlayerHandRank;
				}
			}
		}

		// Evaluate what the best move is
		while(true) {
			// Printing information about the dealer's hand
			System.out.println(toString());
			printCards();

			// Grab our rank
			totalRank = getHand().getTotalRank();

			// Check if we've busted out
			if(totalRank > 21) {
				System.out.println("The dealer has busted!");
				System.out.println("-----------");
				return true;
			}

			// If our hand beats the other player's highest rank, then we'll just stay with what we have, without drawing
			if(totalRank > highestValidRank) {
				System.out.println("Dealer: I'll stay.");
				System.out.println("-----------");
				return false;
			}

			// If our rank is larger than 17, we should stay with what we have.
			if(totalRank >= 17) {
				System.out.println("Dealer: I'll stay.");
				System.out.println("-----------");
				return false;
			} else {
				// Our rank is still lower than one/more of the other players.
				// If it's reasonable that we should draw, then do it; Then check if we've busted
				getHand().draw(deck, 1);
				System.out.println("Dealer: I'll draw.");
				System.out.println("-----------");
			}
		}
	}
}
