package gov.naco.soch.admin.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entry(base = "ou=Users", objectClasses = { "inetOrgPerson", "posixAccount", "top" })
public final class User {
	@Id
	@JsonIgnore
	private Name dn;

	private @Attribute(name = "cn") String fullName;
	private @Attribute(name = "gidNumber") String gidNumber;
	private @Attribute(name = "givenName") String firstName;
	private @Attribute(name = "homeDirectory") String homeDirectory;
	private @Attribute(name = "loginShell") String loginShell;
	private @Attribute(name = "userPassword") String userPassword;
	private @Attribute(name = "sn") String lastName;
	private @Attribute(name = "uidNumber") String uidNumber;
	private @Attribute(name = "uid") String uid;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Name id, String fullName, String gidNumber, String firstName, String homeDirectory, String loginShell,
			String userPassword, String lastName, String uidNumber, String uid) {
		super();
		this.dn = id;
		this.fullName = fullName;
		this.gidNumber = gidNumber;
		this.firstName = firstName;
		this.homeDirectory = homeDirectory;
		this.loginShell = loginShell;
		this.userPassword = userPassword;
		this.lastName = lastName;
		this.uidNumber = uidNumber;
		this.uid = uid;
	}

	public Name getId() {
		return dn;
	}

	public void setId(Name id) {
		this.dn = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGidNumber() {
		return gidNumber;
	}

	public void setGidNumber(String gidNumber) {
		this.gidNumber = gidNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	public String getLoginShell() {
		return loginShell;
	}

	public void setLoginShell(String loginShell) {
		this.loginShell = loginShell;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUidNumber() {
		return uidNumber;
	}

	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
