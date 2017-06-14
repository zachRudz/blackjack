package com.company.cards;

/**
 * Throwable exception to indicate that we have too many cards in our collections (decks, hands, etc).
 * Created by zach on 20/05/17.
 */
public class tooManyCardsInCollectionException extends Exception {
	public tooManyCardsInCollectionException(int maxNumCards) {
		super ("Attempted to add a card, but are already at max number of cards in collection (" + maxNumCards + ")");
	}
}
