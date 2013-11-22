package app.saleBadger.validator;

import java.util.ResourceBundle;

public class ErrorMessagesMapper {
	
	private final static ResourceBundle resource = ResourceBundle.getBundle("ValidationMessages");
	
	public static String getString(String key) {
		return resource.getString(key);
	}

}
