package org.example.ldappoc.service;

import org.example.ldappoc.model.LdapUser;
import org.example.ldappoc.repository.UserRepository;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LdapUserService {

    private final UserRepository userRepository;
    private final LdapTemplate ldapTemplate;

    public LdapUserService(UserRepository userRepository, LdapTemplate ldapTemplate) {
        this.userRepository = userRepository;
        this.ldapTemplate = ldapTemplate;
    }

    // --- Spring Data LDAP Repository approach ---

    public List<LdapUser> findAll() {
        return userRepository.findAll();
    }

    public Optional<LdapUser> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    public List<LdapUser> searchByName(String name) {
        return userRepository.findByFullNameContaining(name);
    }

    public Optional<LdapUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public LdapUser create(LdapUser user) {
        return userRepository.save(user);
    }

    public void delete(LdapUser user) {
        userRepository.delete(user);
    }

    // --- LdapTemplate approach (more flexible queries) ---

    public List<LdapUser> searchByTitle(String title) {
        return ldapTemplate.find(
                LdapQueryBuilder.query()
                        .base("ou=people")
                        .where("title").is(title),
                LdapUser.class
        );
    }

    public List<LdapUser> searchWithFilter(String attribute, String value) {
        return ldapTemplate.find(
                LdapQueryBuilder.query()
                        .base("ou=people")
                        .where(attribute).like("*" + value + "*"),
                LdapUser.class
        );
    }

    // --- Authentication ---

    public boolean authenticate(String uid, String password) {
        try {
            ldapTemplate.authenticate(
                    LdapQueryBuilder.query()
                            .base("ou=people")
                            .where("uid").is(uid),
                    password
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
