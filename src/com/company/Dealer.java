package com.company;

import com.company.cards.Deck;
import com.company.cards.Hand;
import com.company.cards.Card;

/**
 * Created by zach on 23/05/17.
 */
public class Dealer extends Person {
	/*********************
	 * Getters and setters
	 */
	public Card getFirstCard() {
		return getHand().peek(0);
	}
	
	public double getTotalBets() {
		return totalBets;
	}
	
	public void setTotalBets(double totalBets) {
		this.totalBets = totalBets;
	}
	
	public void addToTotalBets(double newBet) {
		this.totalBets += newBet;
	}
	
	// Sum of all bets between all the players
	double totalBets;
}
