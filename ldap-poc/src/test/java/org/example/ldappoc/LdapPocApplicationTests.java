package org.example.ldappoc;

import org.example.ldappoc.model.LdapUser;
import org.example.ldappoc.service.LdapGroupService;
import org.example.ldappoc.service.LdapUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LdapPocApplicationTests {

    @Autowired
    private LdapUserService userService;

    @Autowired
    private LdapGroupService groupService;

    @Test
    void shouldFindAllUsers() {
        List<LdapUser> users = userService.findAll();
        assertEquals(3, users.size());
    }

    @Test
    void shouldFindUserByUid() {
        Optional<LdapUser> user = userService.findByUid("john.doe");
        assertTrue(user.isPresent());
        assertEquals("John Doe", user.get().getFullName());
        assertEquals("john.doe@example.org", user.get().getEmail());
    }

    @Test
    void shouldSearchByName() {
        List<LdapUser> users = userService.searchByName("Smith");
        assertEquals(1, users.size());
        assertEquals("jane.smith", users.getFirst().getUid());
    }

    @Test
    void shouldSearchByTitle() {
        List<LdapUser> users = userService.searchByTitle("DevOps Engineer");
        assertEquals(1, users.size());
        assertEquals("bob.wilson", users.getFirst().getUid());
    }

    @Test
    void shouldAuthenticateWithCorrectPassword() {
        assertTrue(userService.authenticate("john.doe", "password123"));
    }

    @Test
    void shouldRejectWrongPassword() {
        assertFalse(userService.authenticate("john.doe", "wrongpassword"));
    }

    @Test
    void shouldFindGroupsForUser() {
        List<String> groups = groupService.findGroupsForUser("jane.smith");
        assertTrue(groups.contains("developers"));
        assertTrue(groups.contains("admins"));
        assertEquals(2, groups.size());
    }

    @Test
    void shouldGetGroupMembers() {
        Set<String> members = groupService.getMemberUids("developers");
        assertTrue(members.contains("john.doe"));
        assertTrue(members.contains("jane.smith"));
        assertEquals(2, members.size());
    }
}
