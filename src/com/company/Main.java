package com.company;

import com.company.cards.Deck;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    	GameController gc = new GameController();
        Deck deck = new Deck();

       	// After each round, can we say that there has been a winner?
		// Or, has everyone busted out?
        Boolean isGameOver;
	    
        // Initializing dealer
	    Dealer dealer = new Dealer();
	    
        // Initializing players
	    ArrayList<Player> players = new ArrayList();
	    players.add(new HumanPlayer("John", 500.00));
	    players.add(new HumanPlayer("Steve", 500.00));
		players.add(new HumanPlayer("Lilly", 500.00));


	    // Begin playing rounds
		Scanner in = new Scanner(System.in);
		String response;
		Boolean isValidResponse;

		do {
			// Play a single round
			gc.playRound(deck, dealer, players);


			// See if we can play another round
			// ie: Do we have a single player left, or alternatively, has everyone busted out?
			if(gc.isGameOver(players)) {
				System.out.println("See you, space cowboy.");
				return;
			}

			// Keep asking the user until we get a valid response from them
			isValidResponse = false;
			while(!isValidResponse) {
				// Read user's response
				System.out.print("Play again? [y/n]");
				response = in.next();

				if (response.matches("[nN]*")) {
					System.out.println("See you, space cowboy.");
					return;
				} else if (response.matches("[yY]*")) {
					isValidResponse = true;
					System.out.println();
					System.out.println();
					System.out.println();
				} else {
					System.out.println("I didn't understand that.");
				}
			}
		} while(true);
    }
}