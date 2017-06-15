package com.company;

/**
 * Exception to be thrown when we don't have enough funds to do an action
 * Created by zach on 10/06/17.
 */
class NotEnoughFundsException extends Throwable {
	/**
	 * Throwable exception to save us from going into a negative balance
	 * @param requiredFunds How much money we're trying to take
	 * @param funds How much money we actually have
	 */
	NotEnoughFundsException(double requiredFunds, double funds) {
		super(String.format("Not enough funds (Have: $%.2f, need: $%2.f).", requiredFunds, funds));
	}
}
