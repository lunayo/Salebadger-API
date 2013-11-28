package app.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import app.model.constraints.LocationIsValid.LocationValidator;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
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
			ConstraintValidator<LocationIsValid, double[]> {

		@Override
		public void initialize(LocationIsValid location) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isValid(double[] location,
				ConstraintValidatorContext context) {
			// TODO Auto-generated method stub
			if (location.length != 2)
				return false;
			// first variable is longitude, second variable is latitude
			if (location[0] < MIN_LATITUDE || location[0] > MAX_LATITUDE) {
				context.buildConstraintViolationWithTemplate(
						"{product.wrong.location.latitude}")
						.addConstraintViolation();
				return false;
			} else if (location[1] < MIN_LONGITUDE
					|| location[1] > MAX_LONGITUDE) {
				context.buildConstraintViolationWithTemplate(
						"{product.wrong.location.longitude}")
						.addConstraintViolation();
			}
			return true;
		}

	}

}
