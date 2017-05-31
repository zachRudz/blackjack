package com.company;

import com.company.cards.Deck;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    	GameController gc = new GameController();
        Deck deck = new Deck();
	    
        // Initializing dealer
	    Dealer dealer = new Dealer();
	    
        // Initializing players
	    Player players[] = new Player[2];
	    players[0] = new HumanPlayer("John", 500.00);
	    players[1] = new HumanPlayer("Steve", 500.00);


	    // Begin playing rounds
		Scanner in = new Scanner(System.in);
		String response;
		Boolean isValidResponse;

		do {
			// Play a single round
			gc.playRound(deck, dealer, players);

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