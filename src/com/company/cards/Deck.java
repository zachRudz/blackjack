package com.company.cards;

/**
 * Created by zach on 20/05/17.
 */
public class Deck extends CardCollection {
    public Deck() {
        maxNumCards = 52;
        numCards = 52;
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
    public int collectCards(CardCollection source) {
    	while(source.getNumCards() > 0 && numCards < maxNumCards) {
    		push(source.pop());
	    }
	    
	    return source.getNumCards();
    }
}
