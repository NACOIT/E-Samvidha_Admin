package gov.naco.soch.admin.ldap.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import gov.naco.soch.admin.ldap.Role;

public interface RoleLdapRepository extends LdapRepository<Role> {

}
