package app.model.Validator;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD, METHOD})
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {
	
	String message() default "Invalid email address";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
