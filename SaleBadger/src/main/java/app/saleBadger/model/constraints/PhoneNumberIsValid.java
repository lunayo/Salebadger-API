package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import app.saleBadger.model.constraints.PhoneNumberIsValid.PhoneNumberValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumberIsValid {
	
	String message() default "{contact.wrong.phone}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberIsValid, String> {

		@Override
		public void initialize(PhoneNumberIsValid countryCode) {

		}

		@Override
		public boolean isValid(String phoneNumber, ConstraintValidatorContext arg1) {
			
			if (phoneNumber.length() >= 6 && phoneNumber.length() <= 13) {
				return true;
			}
			
			return false;
		}

	}

}
