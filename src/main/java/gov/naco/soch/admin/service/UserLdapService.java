package gov.naco.soch.admin.service;

import java.util.List;

import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import gov.naco.soch.admin.ldap.repository.RoleLdapRepository;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.UserAuth;

@Service
public class UserLdapService {
	@Autowired
	RoleLdapRepository roleLdapRepository;

	@Autowired
	private LdapTemplate ldapTemplate;
	private static final Logger logger = LoggerFactory.getLogger(UserLdapService.class);

	public void saveUserToLdapServer(UserMasterDto userMasterDto, List<RoleDto> roleDtos) {
		try {
			Name dn = LdapNameBuilder.newInstance().add("ou", "Users").add("uid", userMasterDto.getUserName()).build();
			DirContextAdapter context = new DirContextAdapter(dn);

			context.setAttributeValues("objectClass", new String[] { "inetOrgPerson", "posixAccount", "top" });
			context.setAttributeValue("cn",
					String.valueOf(userMasterDto.getFirstname() + " " + userMasterDto.getLastname()));
			String lastName = (userMasterDto.getLastname() == null || userMasterDto.getLastname().isEmpty()) ? "NA"
					: userMasterDto.getLastname();
			context.setAttributeValue("sn", String.valueOf(lastName));
			context.setAttributeValue("uidNumber", String.valueOf(userMasterDto.getId()));
			context.setAttributeValue("gidNumber", String.valueOf(roleDtos.get(0).getId()));
			context.setAttributeValue("givenName", String.valueOf(userMasterDto.getFirstname()));
			context.setAttributeValue("homeDirectory", "/home/users/" + userMasterDto.getUserName());
			context.setAttributeValue("loginShell", "/bin/sh");
			context.setAttributeValue("uid", String.valueOf(userMasterDto.getUserName()));
			context.setAttributeValue("userPassword", String.valueOf(userMasterDto.getPassword()));

			ldapTemplate.bind(context);
		} catch (Exception e) {
			logger.error("LDAP  User Bind Exception : ", e);
		}
	}

	public void deleteUserFromLdap(UserAuth userAuth) {
		try {
			Name dn = LdapNameBuilder.newInstance().add("ou", "Users").add("uid", userAuth.getUsername()).build();
			ldapTemplate.unbind(dn);
		} catch (Exception e) {
			logger.error("LDAP  User Unbind Exception : ", e);
		}
	}

	public void saveRoleToLdapServer(RoleDto roleDto) {
		try {
			Name dn = LdapNameBuilder.newInstance().add("ou", "Roles").add("cn", roleDto.getName()).build();
			DirContextAdapter context = new DirContextAdapter(dn);

			context.setAttributeValues("objectClass", new String[] { "posixGroup", "top" });
			context.setAttributeValue("cn", roleDto.getName());
			context.setAttributeValue("gidNumber", String.valueOf(roleDto.getId()));

			ldapTemplate.bind(context);
		} catch (Exception e) {
			logger.error("LDAP  Role Bind Exception : ", e);
		}
	}

	public void deleteRoleFromLdap(Role role) {
		try {
			Name dn = LdapNameBuilder.newInstance().add("ou", "Roles").add("cn", role.getName()).build();
			ldapTemplate.unbind(dn);
		} catch (Exception e) {
			logger.error("LDAP Role Unbind Exception : ", e);
		}
	}

}
