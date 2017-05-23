package com.company.cards;

/**
 * Created by zach on 20/05/17.
 */
public class tooFewCardsInCollectionException extends Exception {
	public tooFewCardsInCollectionException() {
		super ("No cards left to remove");
	}
}
