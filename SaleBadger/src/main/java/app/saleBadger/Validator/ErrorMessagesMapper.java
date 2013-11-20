package app.saleBadger.Validator;

import java.util.ResourceBundle;

public class ErrorMessagesMapper {
	
	private final static ResourceBundle resource = ResourceBundle.getBundle("ValidationMessages");
	
	public static String getString(String key) {
		return resource.getString(key);
	}

}
