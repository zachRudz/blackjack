package com.company;

import com.company.cards.Deck;

import java.util.ArrayList;

/**
 * The player at the table. This could be a CPU or a human player.
 * Created by zach on 23/05/17.
 */
public abstract class Player extends Person {
	/**************
	 * Constructors
	 * @param name
	 */
	public Player(String name) {
		super();
		this.name = name;
	}
	
	public Player(String name, double startingFunds) {
		super();
		this.name = name;
		this.funds = startingFunds;
	}
	
	
	
	/********************
	 * General operations
	 * @return
	 */
	public String toString() {
		return name + String.format(" [$%.2f]", funds);
	}

	/**
	 * Overridding hand operations
	 */

	
	
	/*********************
	 * Getters and setters
	 * @return
	 */
	// Name
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	// Funds
	public double getFunds() {
		return funds;
	}

	public void setFunds(double funds) {
		this.funds = funds;
	}
	
	public void addFunds(double fundsToAdd) {
		this.funds += fundsToAdd;
	}

	// Bets
	// The player's bet for the k'th round
	public void setBet(double bet) {
		if(bet > funds) {
			throw new ArithmeticException("Not enough funds to bet that much!");
		}
		
		this.bet = bet;
		funds -= bet;
	}
	
	public double getBet() { return bet; }
	public void doubleDown(Dealer dealer) {
		// Throw an exception if we can't double down
		if(bet > funds) {
			throw new ArithmeticException("Not enough funds to double down!");
		}
		
		// Double the bet on our end
		funds -= bet;
		bet *= 2;
	}

	// Winning status
	public String getStatus() {
		return status.toString();
	}

	public void resetStatus() {
		status = null;
	}

	public void evaluateStatus() {
		int rank = getHand().getTotalRank();

		if(rank == 21 && getHand().getNumCards() == 2) {
			// Natural 21
			status = Status.natural;
		} else if(rank > 21) {
			// Busted out
			status = Status.busted;
		} else {
			// Player is safe
			status = Status.safe;
		}
	}

	/********************
	 * Game playing functions that are overwritten by child classes.
	 * Operations would differ depending on if the user were a human or CPU
	 * @return
	 */
	public abstract void placeBet(double minimumBet) throws NotEnoughFundsException;
	protected abstract Boolean play(Deck deck, Dealer dealer, ArrayList<Player> otherPlayerHands);
	

	private String name;
	private double funds;
	private double bet;
	private Status status;

	// After the player has played their round, what is their ending status?
	// Did they bust out?
	// Did they choose to stand?
	// Did they hit a natural (total rank of 21 on the first 2 cards)
	private enum Status {
		busted, safe, natural
	};
}
