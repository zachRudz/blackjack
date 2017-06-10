package com.company;

import com.company.cards.Deck;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zach on 23/05/17.
 */
public class GameController {
	private int bestRank = 0;
	private double minimumBet = 5.00;
	private ArrayList<Player> highestRankPlayers = new ArrayList();


	/**
	 * Play a single game of blackjack, using the players/dealer/deck supplied.
	 * @param deck
	 * @param dealer
	 * @param players
	 * @return True/false, depending on if there's been a winner (or everyone busted out)
	 */
	public void playRound(Deck deck, Dealer dealer, ArrayList players) {
		// Prepare for the round to begin
		deck.shuffle();
		placeBets(dealer, players);
		dealCards(deck, dealer, players);

		// Print the cards of each player, the number of cards in the deck, and the bets of each player.
		printGameInfo(dealer, players);

		// Let each player make their decision, and then the dealer too
		resetRankings();
		play_players(deck, dealer, players);
		int dealerRank = play_dealer(deck, dealer, players);

		// Print the scores of each player + dealer
		printFinalRanks(dealer, players);

		// Compare to see who was the winner.
        handleWinnings(dealerRank, dealer, players);

        // Collect cards from players
		collectCards(deck, dealer, players);

		// Increase the minimum bet
		increaseMinimumBet();

		// Eliminate players if they don't have enough money for the next round
		postGameElimination(players);
	}

	// Print the cards of each player, the number of cards in the deck, and the bets of each player.
	private void printGameInfo(Dealer dealer, ArrayList<Player> players) {
		// Print the pot to be won
		System.out.println(String.format("Pot: $%.2f", dealer.getTotalBets()));

		// Peek at the dealer's hand
		System.out.println("Dealer");
		System.out.println(String.format("Score: [%d + ?]", dealer.getFirstCard().getRankValue()));
		System.out.println("\t" + dealer.getFirstCard());
		System.out.println("\t???");
		System.out.println();

		// Printing each player's hand
		for (Player p : players) {
			System.out.println(p);
			p.getHand().printCards();
			System.out.println();
		}
		System.out.println("=================");
		System.out.println();
	}

	// Deal cards to all players + the dealer
	private void dealCards(Deck deck, Dealer dealer, ArrayList<Player> players) {
		for (Player p : players) {
			p.draw(deck, 2);
		}
		dealer.draw(deck, 2);

		System.out.println();
	}

	// Place the bets for all the players
	private void placeBets(Dealer dealer, ArrayList<Player> players) {
		dealer.setTotalBets(0);

		// Placing bets
		System.out.println(String.format("Place your bets (Minimum bet: $%.2f).", minimumBet));
		for (Player p : players) {
			System.out.println("[" + p.getName() + "]'s turn");

			// Placing the bet
			try {
				p.placeBet(minimumBet);
			} catch (NotEnoughFundsException e) {
				System.out.println("Attempted to place a bet, but the player doesn't meet the minimum funds requirement.");
				System.out.println(e.getMessage());
			}

			// Adding bet to prize pool
			dealer.addToTotalBets(p.getBet());
		}

		// Adding the dealer's contribution
		double dealerBet = dealer.getTotalBets() / players.size();
		dealer.addToTotalBets(dealerBet);
		System.out.println(String.format("Dealer matches the average bet ($%.2f).", dealerBet));

		System.out.println();
	}

	/**
	 * Because we have the rankings and the list of highest ranking players all in member variables,
	 * we need to reset them before each game.
	 */
	private void resetRankings() {
		bestRank = 0;
		highestRankPlayers.clear();
	}

	// Let each player make their turn
	// This means the hit/stand/double down action phase.
	private void play_players(Deck deck, Dealer dealer, ArrayList<Player> players) {
		int currentRank;

		// Letting each player have their turn
		for (Player p : players) {
			// Each player has their turn
			p.play(deck, dealer, players);

			// Printing this player's rank
			currentRank = p.getHand().getTotalRank();

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

	private int play_dealer(Deck deck, Dealer dealer, ArrayList<Player> players) {
		int dealerRank;

		// Let the dealer play
		dealer.play(deck,dealer,players);

		// Get his rank, and print it
		dealerRank = dealer.getHand().getTotalRank();

		return dealerRank;
	}

	/**
	 * Print the ranks of each player's hands
	 * @param dealer
	 * @param players
	 */
	private void printFinalRanks(Dealer dealer, ArrayList<Player> players) {
		System.out.println();
		System.out.println("Final scores:");

		// Printing players' rank
		for(Player p : players) {
			System.out.println(String.format("\t%s [%d]", p.getName(), p.getHand().getTotalRank()));
		}

		// Printing the dealer's rank
		System.out.println(String.format("\tDealer [%d]", dealer.getHand().getTotalRank()));
		System.out.println();
	}


	/**
	 * Test to see who won, if anyone.
	 * @param dealerRank
	 * @param dealer
	 * @param players
	 */
	private void handleWinnings(int dealerRank, Dealer dealer, ArrayList<Player> players) {

		// Seeing if the dealer has the highest rank (without going over 21)
		if (dealerRank > 21) {
			// Dealer busts: Pot split between highest ranked players
			System.out.println("Dealer has busted!");
			System.out.println("Winners:");

			int numWinners = highestRankPlayers.size();
			double winningsPerPlayer = dealer.getTotalBets() / numWinners;
			for (Player p : highestRankPlayers) {
				System.out.println(String.format("\t%s: +$%.2f", p.getName(), winningsPerPlayer));
				p.addFunds(winningsPerPlayer);
			}

			return;
		}

		// Testing if
		// - The dealer won, as well as 1 or more player
		// - The dealer won
		// - No one won
		// - One or more players have won
		if (dealerRank == bestRank) {
			// Dealer matched the highest rank
			// Divide the winnings between all the players + the dealer
			int numWinners = highestRankPlayers.size() + 1;
			double winningsPerPlayer = dealer.getTotalBets() / numWinners;

			// Distribute winnings to players
			for (Player p : highestRankPlayers) {
				System.out.println(String.format("\t%s: +$%.2f", p.getName(), winningsPerPlayer));
				p.addFunds(winningsPerPlayer);
			}

			return;

		} else if (dealerRank > bestRank) {
			// Dealer wins: Pot doesn't go to anyone
			System.out.println("Dealer wins!");

		} else if (highestRankPlayers.size() == 0) {
			// No one wins (Everyone busted)
			System.out.println("Everyone has busted! Bets go back to everyone.");

			for (Player p : players) {
				p.addFunds(p.getBet());
			}
		} else {
			System.out.println("Winners:");
			// One or more players have beaten the dealer
			int numWinners = highestRankPlayers.size();

			double winningsPerPlayer = dealer.getTotalBets() / numWinners;
			for (Player p : highestRankPlayers) {
				System.out.println(String.format("\t%s: +$%.2f", p.getName(), winningsPerPlayer));
				p.addFunds(winningsPerPlayer);
			}
		}
	}


	/**
	 * Collect all of the cards from the players + dealer and put them back in the deck
	 * @param deck
	 * @param dealer
	 * @param players
	 */
	private void collectCards(Deck deck, Dealer dealer, ArrayList<Player> players) {
		deck.collectCards(dealer.getHand());

		for(Player p : players)
			deck.collectCards(p.getHand());
	}


	/**
	 * Check to see who's run out of money. Eliminate players who have won the game, and make final statements if
	 * everyone has busted out, or if one player is left.
	 *
	 * @param players
	 * @return Returns True/false if the game is over (everyone busted out, or 1 player remains).
	 */
	private void postGameElimination(ArrayList<Player> players) {
		System.out.println();

		// Check if any players have run out of funds; If so, eliminate them from the table
		Iterator<Player> p = players.iterator();
		Player tmp;
		while(p.hasNext()) {
			tmp = p.next();

			// Test if each person has enough funds to make the min bet
			if(tmp.getFunds() < minimumBet) {
				System.out.println(tmp.getName() + " doesn't have enough money to make the minimum bet; They have been eliminated!");
				p.remove();
			}
		}

		// Check if we've got 1 player remaining, or if everyone has busted out
		if(players.size() == 0) {
			// Everyone busted out in the last turn
			System.out.println("All players have been eliminated!");
		} else if(players.size() == 1) {
			// One player has won.
			System.out.println(players.get(0).getName() + " is the last player left!");
			System.out.println(players.get(0).getName() + " has won!");
		}
	}


	/**
	 * The minimum bet should increase each round. Double the minimum bet each round.
	 * Should occur before the postGameEliminations, since it relies on the minimum bet of the next round.
	 */
	private void increaseMinimumBet() {
		// Double the minimum bet for the next round
		minimumBet *= 2;
	}


	/**
	 * Check if we've reached the end of the game.
	 * ie: Either only 1 player remains, or all players have busted out
	 * @param players
	 * @return
	 */
	public Boolean isGameOver(ArrayList<Player> players) {
		// Check if we've got 1 player remaining, or if everyone has busted out
		if(players.size() < 2) {
			return true;
		}

		return false;
	}
}
