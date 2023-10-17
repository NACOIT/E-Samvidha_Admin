package gov.naco.soch.admin.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entry(base = "ou=Roles", objectClasses = { "posixGroup", "top" })
public final class Role {
	@Id
	@JsonIgnore
	private Name dn;

	private @Attribute(name = "cn") String roleName;
	private @Attribute(name = "gidNumber") String gidNumber;

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getGidNumber() {
		return gidNumber;
	}

	public void setGidNumber(String gidNumber) {
		this.gidNumber = gidNumber;
	}

}
