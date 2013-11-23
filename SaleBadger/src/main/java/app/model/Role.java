package app.model;

public enum Role {
	ADMIN("admin"),
	USER("user");
	private String roleStirng;

	private Role(String roleString){
		this.roleStirng = roleString;	
	}
	
	@Override
	public String toString(){
		return this.roleStirng;
	}
}
