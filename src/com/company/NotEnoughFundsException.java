package com.company;

/**
 * Exception to be thrown when we don't have enough funds to do an action
 * Created by zach on 10/06/17.
 */
class NotEnoughFundsException extends Throwable {
	NotEnoughFundsException(double minimumRequirement, double funds) {
		super(String.format("Not enough funds (Have: $%.2f, need: $%2.f).", minimumRequirement, funds));
	}
}
