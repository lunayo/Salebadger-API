package app.saleBadger.model;

import java.util.Currency;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import app.saleBadger.model.constraints.CurrencyCodeIsValid;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {

	@NotNull
	private double amount;
	@CurrencyCodeIsValid
	private String currencyCode;
	
	public Price() {
		
	}

	public Price(double amount, String currencyCode) {
		this.amount = amount;
		this.currencyCode = currencyCode;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
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
