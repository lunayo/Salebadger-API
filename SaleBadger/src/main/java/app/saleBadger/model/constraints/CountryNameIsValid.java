package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import app.saleBadger.model.constraints.CountryNameIsValid.CountryNameValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = CountryNameValidator.class)
public @interface CountryNameIsValid {
	
	String message() default "{contact.wrong.country.name}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	public class CountryNameValidator implements ConstraintValidator<CountryNameIsValid, String> {

		@Override
		public void initialize(CountryNameIsValid countryCode) {

		}

		@Override
		public boolean isValid(String countryName, ConstraintValidatorContext arg1) {
			
			// Get locale all countries
			Locale[] locales = Locale.getAvailableLocales();
			for (Locale locale : locales) {
				if (locale.getDisplayCountry().equals(countryName)) {
					return true;
				}
			}
			
			return false;
		}

	}

}
