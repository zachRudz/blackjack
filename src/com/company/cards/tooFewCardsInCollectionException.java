package com.company.cards;

/**
 * A throwable exception to indicate that we have too few cards in a collection to be able to draw.
 * Created by zach on 20/05/17.
 */
public class tooFewCardsInCollectionException extends Exception {
	public tooFewCardsInCollectionException() {
		super ("No cards left to remove");
	}
}
