package app.model.Validator;

public final class ValidationError {
	
	private String message;
	private int responseCode;
	
	public ValidationError(String message, int responseCode) {
		this.setMessage(message);
		this.setResponseCode(responseCode);
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
