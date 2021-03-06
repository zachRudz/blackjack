package com.company.cards;

/**
 * A standard deck of 52 cards
 * Created by zach on 20/05/17.
 */
public class Deck extends CardCollection {
    private static final int NUM_CARDS_IN_STANDARD_DECK = 52;

    public Deck() {
        maxNumCards = NUM_CARDS_IN_STANDARD_DECK;
        numCards = NUM_CARDS_IN_STANDARD_DECK;
        cards = new Card[maxNumCards];

        // Initializing the cards in the deck
        int cardNum = 0;
        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) {
                // Initializing card
                cards[cardNum] = new Card();

                cards[cardNum].setSuit(s);
                cards[cardNum].setRank(r);

                cardNum++;
            }
        }
    }

    // Move all the cards from the source to this deck
	// Returns the number of cards in the source collection that couldn't be collected.
	//  This only would happen if the deck is too full.
    public void collectCards(CardCollection source) {
    	while(source.getNumCards() > 0 && numCards < maxNumCards) {
    		push(source.pop());
	    }

	}
}
