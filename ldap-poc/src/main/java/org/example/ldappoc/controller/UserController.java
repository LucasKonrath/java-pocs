package org.example.ldappoc.controller;

import org.example.ldappoc.model.LdapUser;
import org.example.ldappoc.service.LdapGroupService;
import org.example.ldappoc.service.LdapUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final LdapUserService userService;
    private final LdapGroupService groupService;

    public UserController(LdapUserService userService, LdapGroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping
    public List<LdapUser> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{uid}")
    public ResponseEntity<LdapUser> findByUid(@PathVariable String uid) {
        return userService.findByUid(uid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<LdapUser> search(@RequestParam String name) {
        return userService.searchByName(name);
    }

    @GetMapping("/by-title")
    public List<LdapUser> searchByTitle(@RequestParam String title) {
        return userService.searchByTitle(title);
    }

    @GetMapping("/{uid}/groups")
    public List<String> getUserGroups(@PathVariable String uid) {
        return groupService.findGroupsForUser(uid);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody Map<String, String> credentials) {
        String uid = credentials.get("uid");
        String password = credentials.get("password");
        boolean authenticated = userService.authenticate(uid, password);
        return ResponseEntity.ok(Map.of(
                "uid", uid,
                "authenticated", authenticated
        ));
    }
}
