package app.saleBadger.validator;

import java.util.ArrayList;
import java.util.List;

public final class ErrorValidationMapper {
	
	private List<Error> errors;
	
	public ErrorValidationMapper() {
		errors = new ArrayList<Error>();
	}
	
	public void addError(String errorMessage, int errorCode) {
		Error error = new Error(errorMessage, errorCode);
		errors.add(error);
	}

	public List<Error> getErrors() {
		return errors;
	}
	
	@Override
	public String toString() {
		return "ErrorValidationMapper [errors=" + errors + "]";
	}


	private class Error {
		
		@Override
		public String toString() {
			return "Error [errorMessage=" + errorMessage + ", errorCode="
					+ errorCode + "]";
		}

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

