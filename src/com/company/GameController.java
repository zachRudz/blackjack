package com.company;

import com.company.cards.Deck;

/**
 * Created by zach on 23/05/17.
 */
public class GameController {
	
	public void playRound(Deck deck, Dealer dealer, Player[] players) {
		// Shuffle deck before round begins
		deck.shuffle();
		
		// Placing bets
		System.out.println("Place your bets.");
		for(Player p : players) {
			System.out.println("[" + p.getName() + "]'s turn");
			p.placeBet();
		}
		
		// Deal cards
		for(Player p : players) {
			p.draw(deck, 2);
		}
		dealer.draw(deck, 2);
		
		// Print game info
		printGameInfo(dealer, players);
		
		// Let each player make their decision
		for(Player p : players) {
			System.out.println(p.play(deck, dealer, players));
			System.out.println("Rank: " + p.getHand().getTotalRank());
		}
		
		// Let the dealer play
		//dealer.play(deck, dealer, players);
		
		// Calculate the winner
		
	}
	
	// Print the cards of each player, the number of cards in the deck, and the bets of each player.
	private void printGameInfo(Dealer dealer, Player[] players) {
		System.out.println("Dealer's cards: " + dealer.getFirstCard());
		
		// Printing each player's hand
		for(Player p : players) {
			System.out.println(p);
			p.getHand().printCards();
		}
		System.out.println("=================");
	}
}
