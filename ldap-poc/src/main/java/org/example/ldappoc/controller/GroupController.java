package org.example.ldappoc.controller;

import org.example.ldappoc.model.LdapGroup;
import org.example.ldappoc.service.LdapGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final LdapGroupService groupService;

    public GroupController(LdapGroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<LdapGroup> findAll() {
        return groupService.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<LdapGroup> findByName(@PathVariable String name) {
        return groupService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{name}/members")
    public Set<String> getMembers(@PathVariable String name) {
        return groupService.getMemberUids(name);
    }
}
