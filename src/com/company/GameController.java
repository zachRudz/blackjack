package com.company;

import com.company.cards.Deck;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zach on 23/05/17.
 */
public class GameController {
	private double minimumBet = 5.00;


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
		play_players(deck, dealer, players);
		play_dealer(deck, dealer, players);

		// Print the scores of each player + dealer
		printFinalRanks(dealer, players);

		// Compare to see who was the winner.
        handleWinnings(dealer, players);

        // Collect cards from players
		collectCards(deck, dealer, players);

		// Increase the minimum bet
		increaseMinimumBet();

		// Eliminate players if they don't have enough money for the next round
		postGameElimination(players);
	}

	// Print the cards of each player, the number of cards in the deck, and the bets of each player.
	private void printGameInfo(Dealer dealer, ArrayList<Player> players) {
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
		}

		System.out.println();
	}


	// Let each player make their turn
	// This means the hit/stand/double down action phase.
	private void play_players(Deck deck, Dealer dealer, ArrayList<Player> players) {
		int currentRank;

		// Letting each player have their turn
		for (Player p : players) {
			// Reset the player's status before they play
			p.resetStatus();

			// Each player has their turn
			p.play(deck, dealer, players);

			// Mark down the player's status for this game (Safe? Busted out? Doubled down, and is safe? Natural 21?)
			p.evaluateStatus();
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
	 * Distribute winnings amongst players
	 * @param dealer
	 * @param players
	 */
	private void handleWinnings(Dealer dealer, ArrayList<Player> players) {
		int dealerRank = dealer.getHand().getTotalRank();

		// Handle the winnings for each player
		System.out.println("Winnings: ");
		for(Player p : players) {
			int playerRank = p.getHand().getTotalRank();
			System.out.print("\t");

			// Evaluating what rewards the player will get from this round
			if(p.getStatus() == "safe" && playerRank == dealerRank)  {
				// Push: Player is safe, AND they matched the dealer's hand
				// Bets go back to the player.
				System.out.println(String.format("%s: Push (+ $%.2f).", p.getName(), p.getBet()));
				p.addFunds(p.getBet());

			} else if(p.getStatus() == "natural") {
				// Natural: Player has hit 21 on their opening hand
				// Player gets their original bet, as well as an additional 1.5 times their bet from the dealer
				System.out.println(String.format("%s: Natural blackjack (+ $%.2f).", p.getName(), p.getBet() * 2.5));
				p.addFunds(p.getBet() * 2.5);

			} else if(p.getStatus() == "safe" && playerRank > dealerRank) {
				// Beat the dealer: Player is safe, and they beat the dealer
				// Player gets their original bet, as well as the dealer matching their bet.
				System.out.println(String.format("%s: Beat the dealer (+ $%.2f).", p.getName(), p.getBet() * 2));
				p.addFunds(p.getBet() * 2);

			} else if(p.getStatus() == "safe" && dealerRank > 21) {
				// Beat the dealer: Dealer busted out, and the player is safe.
				// Player gets their original bet, as well as the dealer matching their bet.
				System.out.println(String.format("%s: Beat the dealer (+ $%.2f).", p.getName(), p.getBet() * 2));
				p.addFunds(p.getBet() * 2);


				// -- Failure scenarios below --
			} else if(p.getStatus() == "busted") {
				// Player has busted out
				System.out.println(String.format("%s: Busted out(+ $0.00).", p.getName()));

			} else {
				// The dealer has beaten the player.
				System.out.println(String.format("%s: Beaten by the dealer (+ $0.00).", p.getName()));
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
