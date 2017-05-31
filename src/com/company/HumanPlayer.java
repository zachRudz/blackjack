package com.company;

import java.util.Scanner;

import com.company.Dealer;
import com.company.Player;
import com.company.cards.Deck;

/**
 * Created by zach on 23/05/17.
 */
public class HumanPlayer extends Player {
	public HumanPlayer(String name, double initialFunds) {
		super(name, initialFunds);
	}
	
	/*******************
	 * Before starting a round, we must place an initial bet.
	 * Stdio operation to get the bet from the user
	 * @return
	 */
	public double placeBet() {
		Scanner in = new Scanner(System.in);
		double betAmount;
		
		while(true) {
			System.out.print(String.format("What do you want to bet? (Funds: $%.2f) ", getFunds()));
			betAmount = in.nextDouble();
			
			if(betAmount > getFunds()) {
				System.out.println("You don't have enough funds to bet that much.");
			} else {
				setBet(betAmount);
				System.out.println(String.format("%s: I'll bet $%.2f.", getName(), betAmount));
				return betAmount;
			}
		}
	}
	
	/**********************
	 * Perform the interactive part of the game
	 * IE: Hit/stand/stay/double down
	 * @param deck
	 * @param dealer
	 * @param otherPlayers
	 */
	public Boolean play(Deck deck, Dealer dealer, Player[] otherPlayers) {
		System.out.println();

		// Checking if we started out with blackjack
		if(getHand().getTotalRank() == 21) {
			// Print the hand of this person
			System.out.println(toString());
			printCards();

			System.out.println(String.format("%s got blackjack!", getName()));
			System.out.println("-----------");
			return false;
		}

		Boolean canDoubleDown = true;
		int handRank;
		
		Boolean isValidInput;
		String choice;
		Scanner in = new Scanner(System.in);


		// Checking if we have enough funds to double down
		if(getFunds() < getBet())
			canDoubleDown = false;
		
		do {
			// Make sure that input is valid before continuing
			isValidInput = false;
			while(!isValidInput) {
				// Printing the user's info
				System.out.println(toString());
				printCards();



				// Printing the user prompt
				if (canDoubleDown)
					System.out.println("[H]it / [S]tay / [D]ouble down");
				else
					System.out.println("[H]it / [S]tay");
				
				// Wait for stdin
				System.out.print("What do you want to do? ");
				choice = in.next();


				// Testing input
				if (choice.matches("[sS]*")) {
					// Stay: End your turn
					System.out.println(getName() + ": I'll stay.");
					System.out.println("-----------");
					return false;
					
				} else if (choice.matches("[hH]*")) {
					// Hit: Draw a card
					System.out.println(getName() + ": Hit me.");
					System.out.println("-----------");
					getHand().draw(deck, 1);
					isValidInput = true;
					
				} else if (canDoubleDown && choice.matches("[dD]*")) {
					// Double down: Double bet, draw one card, and stay/bust
					System.out.println(getName() + ": Double down.");
					System.out.println("-----------");
					
					// Drawing a card
					getHand().draw(deck, 1);
					
					// Doubling the bet for this player
					doubleDown(dealer);
					
					// Evaluate cards
					System.out.println(toString());
					printCards();
					
					if(getHand().getTotalRank() > 21) {
						System.out.println(getName() + " has busted out!");
						System.out.println("-----------");
						return true;
					} else {
						System.out.println(getName() + " is safe!");
						System.out.println("-----------");
						return false;
					}
					
				} else {
					// No recognizable input
					System.out.println("Bad input. ");
				}
			} // Only continue if input is valid

			// Tally up new score
			handRank = getHand().getTotalRank();

			// Can only double down on the first turn; Disable the ability to do so
			canDoubleDown = false;
		} while(handRank < 22); // Continue if we're still under 21 (and user didn't stay)
		
		// Our hand is greater than 21; We've busted out.
		System.out.println(toString());
		printCards();
		System.out.println(getName() + " has busted out!");
		System.out.println("-----------");
		return true;
	}

}
