package app.saleBadger.model;

import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

import app.saleBadger.model.Price;

public class PriceTest {
	Locale englandLocale = new Locale("en", "GB");
	Currency currency = Currency.getInstance(englandLocale);
	
	Price price = new Price(100.23,currency.getCurrencyCode());
	@Test
	public void test() {
		System.out.println(price);
	}

}
