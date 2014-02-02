package app.saleBadger.model;

import app.saleBadger.model.constraints.CountryCodeIsValid;
import app.saleBadger.model.constraints.CountryIsValid;
import app.saleBadger.model.constraints.CountryNameIsValid;
import app.saleBadger.model.constraints.PhoneNumberIsValid;

@CountryIsValid.List({
	@CountryIsValid(countryCode = "countryCode", countryName = "countryName")
})
public class Contact {

	@CountryCodeIsValid
	private String countryCode;
	@PhoneNumberIsValid
	private String phoneNumber;
	@CountryNameIsValid
	private String countryName;
	
	public Contact() {
		
	}
	
	public Contact(String countryCode, String countryName, String phoneNumber) {
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.phoneNumber = phoneNumber;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Override
	public String toString() {
		return "Contact [countryCode=" + countryCode + ", phoneNumber="
				+ phoneNumber + ", countryName=" + countryName + "]";
	}

	
}
