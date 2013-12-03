package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import app.saleBadger.model.constraints.LocationIsValid.LocationValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER })
@Constraint(validatedBy = LocationValidator.class)
public @interface LocationIsValid {

	public final static double MAX_LONGITUDE = 180;
	public final static double MIN_LONGITUDE = -180;
	public final static double MAX_LATITUDE = 90;
	public final static double MIN_LATITUDE = -90;

	String message() default "{product.wrong.location}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class LocationValidator implements
			ConstraintValidator<LocationIsValid, List<Double>> {

		@Override
		public void initialize(LocationIsValid location) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isValid(List<Double> location,
				ConstraintValidatorContext context) {
			// TODO Auto-generated method stub
			boolean isValid = true;
			context.disableDefaultConstraintViolation();
			if (location.size() != 2)
				return false;
			// first variable is longitude, second variable is latitude
			if (location.get(0) < MIN_LATITUDE || location.get(0) > MAX_LATITUDE) {
				context.buildConstraintViolationWithTemplate(
						"{product.wrong.location.latitude}")
						.addConstraintViolation();
				isValid = false;
			} 
			if (location.get(1) < MIN_LONGITUDE
					|| location.get(1) > MAX_LONGITUDE) {
				context.buildConstraintViolationWithTemplate(
						"{product.wrong.location.longitude}")
						.addConstraintViolation();
				isValid = false;
			}
			return isValid;
		}

	}

}
