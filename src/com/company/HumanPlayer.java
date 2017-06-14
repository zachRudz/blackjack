package com.company;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import com.company.cards.Deck;
import com.company.cards.Hand;
import com.company.cards.tooFewCardsInCollectionException;

/**
 * A human playing a game of blackjack.
 * Created by zach on 23/05/17.
 */
public class HumanPlayer extends Player {
	HumanPlayer(String name, double initialFunds) {
		super(name, initialFunds);
	}

	/*******************
	 * Before starting a round, we must place an initial bet.
	 * Stdio operation to get the bet from the user
	 */
	public void placeBet(double minimumBet) throws NotEnoughFundsException {
		Scanner in = new Scanner(System.in);
		double betAmount;

		// Make sure that we have enough funds to actually place a bet
		if(getFunds() < minimumBet) {
			throw new NotEnoughFundsException(minimumBet, getFunds());
		}

		while(true) {
			System.out.print(String.format("What do you want to bet? (Funds: $%.2f) ", getFunds()));

			// Make sure that the user didn't input trash input
			// ie: 100.00.00
			// ie: Hello
			// ie: Hanzo contributes to the wellbeing of a balanced team
			if(in.hasNextDouble()) {
				// Fetch the user's input
				betAmount = in.nextDouble();

				// Truncate the bet amount such that it is in proper monetary format ($xxxx.xx)
				DecimalFormat df = new DecimalFormat("#.00");
				betAmount = new Double(df.format(betAmount));

				// Testing if the bet amount is valid
				if(betAmount > getFunds()) {
					System.out.print("You don't have enough funds to bet that much. ");
				} else if(betAmount < minimumBet) {
					System.out.print(String.format("You must bet at least the minimum bet ($%.2f) ", minimumBet));
				} else {
					getHand(0).setBet(betAmount);
					System.out.println(String.format("%s: I'll bet $%.2f.", getName(), betAmount));
					return;
				}
			} else {
				// Trash input, reject it and try again.
				in.next();
				System.out.print("I didn't understand that. ");
			}
		}
	}
	
	/**********************
	 * Perform the interactive part of the game
	 * IE: Hit/stand/stay/double down
	 * @param deck The deck that will be used to play the game
	 * @param dealer The dealer that the players will play against
	 * @param otherPlayers The collection of players that will play against the dealer
	 */
	public void play(Deck deck, Dealer dealer, ArrayList<Player> otherPlayers) {
		System.out.println();


		// When the player starts their turn, they begin with a single hand.
		// If they decide to split, then they will get an additional hand.
		int handNum;
		for (handNum = 0; handNum < getNumHands(); handNum++) {
			Hand currHand = getHand(handNum);

			// Checking if we started out with blackjack
			if(testForNatural(currHand)) {
				// Print the current hand
				System.out.println(toString());
				System.out.println(String.format(" Hand [%d / %d]", handNum+1, getNumHands()));
				currHand.printCards();

				System.out.println(String.format("%s got blackjack!", getName()));
				System.out.println("-----------");

			} else {
				// No natural blackjack; We must play the round as per usual

				Boolean canDoubleDown = true;
				Boolean continuePlayingRound = true;
				int handRank;

				Boolean isValidInput;
				String choice;
				Scanner in = new Scanner(System.in);


				// Checking if we have enough funds to double down
				if (getFunds() < currHand.getBet())
					canDoubleDown = false;

				do {
					// Make sure that input is valid before continuing
					isValidInput = false;
					while (!isValidInput) {
						// Printing the user's info
						System.out.print(toString());
						System.out.println(String.format(" Hand [%d / %d]", handNum+1, getNumHands()));
						currHand.printCards();


						// Printing the user prompt
						System.out.print("[H]it / [S]tay");
						if (canDoubleDown)
							System.out.print(" / [D]ouble down");
						if (currHand.canSplit())
							System.out.print(" / [X] Split");
						System.out.println();

						// Wait for stdin
						System.out.print("What do you want to do? ");
						choice = in.next();


						// Testing input
						if (choice.matches("[sS]*")) {
							// Stay: End your turn
							System.out.println(getName() + ": I'll stay.");
							System.out.println("-----------");
							isValidInput = true;
							continuePlayingRound = false;

						} else if (choice.matches("[hH]*")) {
							// Hit: Draw a card
							System.out.println(getName() + ": Hit me.");
							System.out.println("-----------");
							currHand.draw(deck, 1);
							isValidInput = true;

						} else if (canDoubleDown && choice.matches("[dD]*")) {
							// Double down: Double bet, draw one card, and stay/bust
							doubleDown(deck, currHand);
							isValidInput = true;
							continuePlayingRound = false;

						} else if (currHand.canSplit() && choice.matches("[xX]*")) {
							// Split: Add a hand and split.
							split(deck, currHand);

							// Decrementing the hand counter.
							// Because we just added a new card to our original hand, we could possibly be in a position to
							// split again.
							handNum--;

							// Printing all of the user's hands now that they've split.
							printCards();
							System.out.println("-----------");

							isValidInput = true;
							continuePlayingRound = false;

						} else {
							// No recognizable input
							System.out.println("Bad input. ");
						}
					} // Only continue if input is valid

					// Can only double down on the first turn; Disable the ability to do so
					canDoubleDown = false;

					// Tally up new score
					handRank = currHand.getTotalRank();

					// Test if we've busted.
					if(handRank > 21)
						continuePlayingRound = false;

				} while (continuePlayingRound); // Continue if we're still under 21 (and user didn't stay)

				// Our hand is greater than 21; We've busted out.
				if(handRank > 21) {
					System.out.println(toString());
					currHand.printCards();
					System.out.println(getName() + " has busted out!");
					System.out.println("-----------");
				}
			}
		}
	}


	/**
	 * Test whether or not our hand is a natural (rank of 21 with only 2 cards, eg: 10H + AS).
	 * @param currHand The current hand that we want to test for a natural
	 * @return True if we've got a natural blackjack, false otherwise
	 */
	private Boolean testForNatural(Hand currHand) {
		return currHand.getTotalRank() == 21 && currHand.getNumCards() == 2;
	}


	/**
	 * Perform the actions required for a double down.
	 * @param deck The deck that will be used to play the game
	 * @param currHand The hand to double down with
	 */
	private void doubleDown(Deck deck, Hand currHand) {
		System.out.println(getName() + ": Double down.");
		System.out.println("-----------");

		// Drawing a card
		currHand.draw(deck, 1);

		// Handling the betting half of the double down
		currHand.doubleDown();

		// Evaluate cards
		System.out.println(toString());
		printCards();

		// Printing information about the result of the double down
		if (currHand.getTotalRank() > 21) {
			System.out.println(getName() + " has busted out!");
			System.out.println("-----------");

		} else {
			System.out.println(getName() + " is safe!");
			System.out.println("-----------");
		}

	}

	private void split(Deck deck, Hand currHand) {
		System.out.println(getName() + ": I'll split.");
		System.out.println();

		// Performing the split
		try {
			super.split(deck, currHand);

		} catch (tooFewCardsInCollectionException e) {
			System.err.println("Error: Wasn't able to split. Reason: Too few cards. ");
			System.err.println("\tDeck count: " + deck.getNumCards() + ")");
			System.err.println("\tOriginal hand count: " + currHand.getNumCards() + ")");
			e.printStackTrace();
		}
	}
}
