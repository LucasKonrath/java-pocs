package org.example.ldappoc.repository;

import org.example.ldappoc.model.LdapUser;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends LdapRepository<LdapUser> {

    Optional<LdapUser> findByUid(String uid);

    List<LdapUser> findByFullNameContaining(String name);

    Optional<LdapUser> findByEmail(String email);

    List<LdapUser> findByLastName(String lastName);
}
