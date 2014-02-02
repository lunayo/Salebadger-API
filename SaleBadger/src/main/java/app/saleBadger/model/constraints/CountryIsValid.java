package app.saleBadger.model.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.commons.beanutils.BeanUtils;

import app.saleBadger.model.constraints.CountryIsValid.CountryValidator;

@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
@Constraint(validatedBy = CountryValidator.class)
public @interface CountryIsValid {
	
	String message() default "{contact.wrong.country}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	String countryCode();
	 
    String countryName();
	
	@Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @interface List
    {
		CountryIsValid[] value();
    }
	
	public class CountryValidator implements ConstraintValidator<CountryIsValid, Object> {
		
		private String countryCode;
		private String countryName;

		@Override
		public void initialize(CountryIsValid country) {
			this.countryCode = country.countryCode();
			this.countryName = country.countryName();
		}

		@Override
		public boolean isValid(Object country, ConstraintValidatorContext arg1) {
			
			try {
				String countryCodeObject = BeanUtils.getProperty(country, countryCode);
				String countryNameObject = BeanUtils.getProperty(country, countryName);
				
				if (countryCodeObject == null || countryNameObject == null)
					return false;
				
				// check if country code matches country name
				// get the country locale
				Locale[] locales = Locale.getAvailableLocales();
				Locale inputLocale = null;
				for (Locale locale : locales) {
					if (locale.getCountry().equals(countryCodeObject)) {
						inputLocale = locale;
						break;
					}
				}
				
				if (inputLocale == null)
					return false;
				
				if (inputLocale.getDisplayCountry().equals(countryNameObject)) {
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}

	}
	
}
