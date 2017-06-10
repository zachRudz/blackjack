package com.company;

/**
 * Created by zach on 10/06/17.
 */
public class NotEnoughFundsException extends Throwable {
	public NotEnoughFundsException(double minimumRequirement, double funds) {
		super(String.format("Not enough funds (Have: $%.2f, need: $%2.f.", minimumRequirement, funds));
	}
}
