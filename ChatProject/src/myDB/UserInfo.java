package myDB;

public class UserInfo {
	String name;
	boolean ageDisp;
	String bio;
	int age;
	String sex;
	boolean mailDisp;
	String mail;
	
	public UserInfo(String name, boolean ageDisp, String bio, int age, String sex, boolean mailDisp, String mail) {
		this.name = name == null ? "" : name;
		this.ageDisp = ageDisp;
		this.bio = bio == null ? "" : bio;
		this.age = age;
		this.sex = sex;
		this.mailDisp = mailDisp;
		this.mail = mail;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean ageDisp() {
		return ageDisp;
	}
	
	public String getBio() {
		return bio;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getSex() {
		return sex;
	}
	
	public boolean mailDisp() {
		return mailDisp;
	}
	
	public String getMail() {
		return mail;
	}
}