package org.example.ldappoc.runner;

import org.example.ldappoc.model.LdapUser;
import org.example.ldappoc.service.LdapGroupService;
import org.example.ldappoc.service.LdapUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);

    private final LdapUserService userService;
    private final LdapGroupService groupService;

    public DemoRunner(LdapUserService userService, LdapGroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public void run(String... args) {
        log.info("=== LDAP POC Demo ===");

        // 1. List all users
        log.info("--- All Users ---");
        userService.findAll().forEach(user -> log.info("  {}", user));

        // 2. Find user by UID
        log.info("--- Find by UID: john.doe ---");
        userService.findByUid("john.doe")
                .ifPresent(user -> log.info("  Found: {}", user));

        // 3. Search by name
        log.info("--- Search by name containing 'Smith' ---");
        userService.searchByName("Smith").forEach(user -> log.info("  {}", user));

        // 4. Search by title using LdapTemplate
        log.info("--- Search by title: 'Tech Lead' ---");
        userService.searchByTitle("Tech Lead").forEach(user -> log.info("  {}", user));

        // 5. Authenticate users
        log.info("--- Authentication ---");
        log.info("  john.doe with correct password: {}", userService.authenticate("john.doe", "password123"));
        log.info("  john.doe with wrong password: {}", userService.authenticate("john.doe", "wrongpassword"));

        // 6. List all groups
        log.info("--- All Groups ---");
        groupService.findAll().forEach(group -> log.info("  {}", group));

        // 7. Find groups for a user
        log.info("--- Groups for jane.smith ---");
        List<String> groups = groupService.findGroupsForUser("jane.smith");
        groups.forEach(group -> log.info("  {}", group));

        // 8. Get members of a group
        log.info("--- Members of 'developers' group ---");
        groupService.getMemberUids("developers").forEach(uid -> log.info("  {}", uid));

        log.info("=== LDAP POC Demo Complete ===");
        log.info("REST API available at http://localhost:8080/api/users and /api/groups");
    }
}
