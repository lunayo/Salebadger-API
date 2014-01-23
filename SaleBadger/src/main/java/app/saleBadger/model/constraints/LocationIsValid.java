package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.data.mongodb.core.geo.Point;

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
			ConstraintValidator<LocationIsValid, Point> {

		@Override
		public void initialize(LocationIsValid location) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isValid(Point location,
				ConstraintValidatorContext context) {
			// TODO Auto-generated method stub
			boolean isValid = true;
			context.disableDefaultConstraintViolation();

			// first variable is longitude, second variable is latitude
			if (location.getX() < MIN_LATITUDE || location.getX() > MAX_LATITUDE) {
				context.buildConstraintViolationWithTemplate(
						"{product.wrong.location.latitude}")
						.addConstraintViolation();
				isValid = false;
			} 
			if (location.getY() < MIN_LONGITUDE
					|| location.getY() > MAX_LONGITUDE) {
				context.buildConstraintViolationWithTemplate(
						"{product.wrong.location.longitude}")
						.addConstraintViolation();
				isValid = false;
			}
			return isValid;
		}

	}

}
