package com.company;

import com.company.cards.Deck;

import java.util.ArrayList;

/**
 * Created by zach on 23/05/17.
 */
public class GameController {
	private int bestRank = 0;
	private ArrayList<Player> highestRankPlayers = new ArrayList();

	public void playRound(Deck deck, Dealer dealer, Player[] players) {
		// Prepare for the round to begin
		deck.shuffle();
		placeBets(deck, players);
		dealCards(deck, dealer, players);

		// Print the cards of each player, the number of cards in the deck, and the bets of each player.
		printGameInfo(dealer, players);

		// Let each player make their decision, and then the dealer too
		play_players(deck, dealer, players);
		int dealerRank = play_dealer(deck, dealer, players);


		// Compare to see who was the winner.
        handleWinnings(dealerRank, dealer);
	}

	// Print the cards of each player, the number of cards in the deck, and the bets of each player.
	private void printGameInfo(Dealer dealer, Player[] players) {
		// Peek at the dealer's hand
		System.out.println("Dealer's cards: " + dealer.getFirstCard());

		// Printing each player's hand
		for (Player p : players) {
			System.out.println(p);
			p.getHand().printCards();
		}
		System.out.println("=================");
	}

	// Deal cards to all players + the dealer
	private void dealCards(Deck deck, Dealer dealer, Player[] players) {
		for (Player p : players) {
			p.draw(deck, 2);
		}
		dealer.draw(deck, 2);
	}

	// Place the bets for all the players
	private void placeBets(Deck deck, Player[] players) {
		// Placing bets
		System.out.println("Place your bets.");
		for (Player p : players) {
			System.out.println("[" + p.getName() + "]'s turn");
			p.placeBet();
		}
	}

	// Let each player make their turn
	// This means the hit/stand/double down action phase.
	private void play_players(Deck deck, Dealer dealer, Player[] players) {
		int currentRank;

		// Letting each player have their turn
		for (Player p : players) {
			// Each player has their turn
			System.out.println(p.play(deck, dealer, players));

			// Printing this player's rank
			currentRank = p.getHand().getTotalRank();
			System.out.println("Rank: " + currentRank);

			// Seeing if the p'th player has the highest rank (without going over 21)
			if (currentRank < 22) {
				if (currentRank > bestRank) {
					// New highest rank.
					bestRank = currentRank;

					// Clear all the players in our wining list,
					// and add the player to the list
					highestRankPlayers.clear();
					highestRankPlayers.add(p);
				} else if (currentRank == bestRank) {
					// Another player has beaten the highest rank; Add them to the winning player's list
					highestRankPlayers.add(p);
				}
			}
		}
	}

	private int play_dealer(Deck deck, Dealer dealer, Player[] players) {
		int dealerRank;

		// Let the dealer play
		dealer.play(deck,dealer,players);

		// Get his rank, and print it
		dealerRank = dealer.getHand().getTotalRank();
		System.out.println("Rank: " + dealerRank);

		return dealerRank;
	}

	// Test to see who won, if anyone.
	private void handleWinnings(int dealerRank, Dealer dealer) {

		// Seeing if the dealer has the highest rank (without going over 21)
		if (dealerRank > 21) {
			// Dealer busts: Pot split between highest ranked players
			System.out.println("Dealer has busted!");

			int numWinners = highestRankPlayers.size();
			double winningsPerPlayer = dealer.getTotalBets() / numWinners;
			for (Player p : highestRankPlayers) {
				p.addFunds(winningsPerPlayer);
			}

			return;
		}

		// Testing if the dealer won, no one won, or if one or more players have won
		if (dealerRank > bestRank) {
			// Dealer wins: Pot doesn't go to anyone
			System.out.println("Dealer wins!");
			return;
		} else if (bestRank == 0) {
			// No one wins (Everyone busted)
			System.out.println("Everyone has busted! Bets go back to everyone.");
			return;
		} else if (dealerRank > 21) {
			// One or more players have beaten the dealer


			int numWinners = highestRankPlayers.size();
			double winningsPerPlayer = dealer.getTotalBets() / numWinners;
			for (Player p : highestRankPlayers) {
				p.addFunds(winningsPerPlayer);
			}
		} else {

		}

	}
}
