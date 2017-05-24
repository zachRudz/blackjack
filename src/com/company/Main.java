package com.company;

import com.company.cards.Deck;

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
	    
	    
	    gc.playRound(deck, dealer, players);
    }
}