package com.company.cards;

/**
 * A single standard card
 * Created by zach on 20/05/17.
 */
public class Card {
    public enum Suit {
        HEART, SPADE, CLUB, DIAMOND
    }

    public enum Rank {
        ACE     (1),
        TWO     (2),
        THREE   (3),
        FOUR    (4),
        FIVE    (5),
        SIX     (6),
        SEVEN   (7),
        EIGHT   (8),
        NINE    (9),
        TEN     (10),
        JACK    (10),
        QUEEN   (10),
        KING    (10);
        
        // Score of the rank
        private final int rankValue;
        
        Rank(int rankValue) {
        	this.rankValue = rankValue;
        }
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }
    
	public int getRankValue() {
		return rank.rankValue;
	}

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    private Suit suit;
    private Rank rank;
}
