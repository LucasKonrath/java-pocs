package org.example.ldappoc.service;

import org.example.ldappoc.model.LdapGroup;
import org.example.ldappoc.model.LdapUser;
import org.example.ldappoc.repository.GroupRepository;
import org.springframework.stereotype.Service;

import javax.naming.ldap.LdapName;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LdapGroupService {

    private final GroupRepository groupRepository;
    private final LdapUserService userService;

    public LdapGroupService(GroupRepository groupRepository, LdapUserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    public List<LdapGroup> findAll() {
        return groupRepository.findAll();
    }

    public Optional<LdapGroup> findByName(String name) {
        return groupRepository.findByName(name);
    }

    public List<String> findGroupsForUser(String uid) {
        return groupRepository.findAll().stream()
                .filter(group -> group.getMembers() != null && group.getMembers().stream()
                        .anyMatch(member -> member.toString().contains("uid=" + uid)))
                .map(LdapGroup::getName)
                .collect(Collectors.toList());
    }

    public Set<String> getMemberUids(String groupName) {
        return groupRepository.findByName(groupName)
                .map(group -> group.getMembers().stream()
                        .map(member -> {
                            String memberDn = member.toString();
                            int start = memberDn.indexOf("uid=") + 4;
                            int end = memberDn.indexOf(",", start);
                            return end > start ? memberDn.substring(start, end) : memberDn.substring(start);
                        })
                        .collect(Collectors.toSet()))
                .orElse(Set.of());
    }
}
