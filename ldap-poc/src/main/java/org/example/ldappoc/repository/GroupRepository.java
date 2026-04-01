package org.example.ldappoc.repository;

import org.example.ldappoc.model.LdapGroup;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.Optional;

public interface GroupRepository extends LdapRepository<LdapGroup> {

    Optional<LdapGroup> findByName(String name);
}
