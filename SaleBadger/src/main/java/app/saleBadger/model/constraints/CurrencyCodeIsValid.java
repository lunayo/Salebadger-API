package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Currency;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import app.saleBadger.model.constraints.CurrencyCodeIsValid.CurrencyCodeValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = CurrencyCodeValidator.class)
public @interface CurrencyCodeIsValid {

	String message() default "{product.wrong.price.currencyCode}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCodeIsValid, String> {

		@Override
		public void initialize(CurrencyCodeIsValid currencyCode) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isValid(String currencyCode, ConstraintValidatorContext arg1) {
			// TODO Auto-generated method stub
			if (Currency.getInstance(currencyCode) == null) {
				return false;
			}
			
			return true;
		}

	}

}
