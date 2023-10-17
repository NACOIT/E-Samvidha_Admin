package gov.naco.soch.admin.ldap.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import gov.naco.soch.admin.ldap.User;

public interface UserLdapRepository extends LdapRepository<User> {
	User findByUid(String username);

}
