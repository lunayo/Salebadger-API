package app.model.Validator;

import java.util.ArrayList;
import java.util.List;

public final class ValidationError {
	
	private List<Error> errors;
	
	public ValidationError() {
		errors = new ArrayList<Error>();
	}
	
	public void addError(String errorMessage, int errorCode) {
		Error error = new Error(errorMessage, errorCode);
		errors.add(error);
	}

	public List<Error> getErrors() {
		return errors;
	}


	private class Error {
		
		private String errorMessage;
		private int errorCode;
		
		private Error(String errorMessage, int errorCode) {
			this.errorMessage = errorMessage;
			this.errorCode = errorCode;
		}
		
		@SuppressWarnings("unused")
		public String getErrorMessage() {
			return errorMessage;
		}

		@SuppressWarnings("unused")
		public int getErrorCode() {
			return errorCode;
		}

	}

}

