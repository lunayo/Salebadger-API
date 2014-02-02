package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Locale;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import app.saleBadger.model.constraints.CountryCodeIsValid.CountryCodeValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = CountryCodeValidator.class)
public @interface CountryCodeIsValid {
	
	String message() default "{contact.wrong.country.code}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	public class CountryCodeValidator implements ConstraintValidator<CountryCodeIsValid, String> {

		@Override
		public void initialize(CountryCodeIsValid countryCode) {

		}

		@Override
		public boolean isValid(String countryCode, ConstraintValidatorContext arg1) {
			
			// get all the country code locale
			String[] countryCodes = Locale.getISOCountries();
			// search if the locale contains the country code
			if (Arrays.asList(countryCodes).contains(countryCode)) {
				return true;
			}
			
			return false;
		}

	}
	
}
