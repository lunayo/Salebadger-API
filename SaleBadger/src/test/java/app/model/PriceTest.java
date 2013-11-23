package app.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

public class PriceTest {
	Locale englandLocale = new Locale("en", "GB");
	Currency currency = Currency.getInstance(englandLocale);
	
	Price price = new Price(100.23,currency.getCurrencyCode());
	@Test
	public void test() {
		System.out.println(price);
	}

}
