package com.company.cards;

/**
 * Created by zach on 20/05/17.
 */
public class tooManyCardsInCollectionException extends Exception {
	public tooManyCardsInCollectionException(int maxNumCards) {
		super ("Attempted to add a card, but are already at max number of cards in collection (" + maxNumCards + ")");
	}
}
