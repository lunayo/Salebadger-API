package app.model;

import java.util.Currency;

public class Price {

	private double amount;
	private String currencyCode;

	public Price(double amount, String currencyCode) {
		this.amount = amount;
		this.currencyCode = currencyCode;
	}

	public double getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return Currency.getInstance(currencyCode);
	}

	@Override
	public String toString() {
		return "Price [amount=" + getCurrency().getSymbol() + amount + ", currencyCode=" + currencyCode
				+ "]";
	}

}
