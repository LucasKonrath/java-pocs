package org.example.ldappoc.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.Set;

@Entry(base = "ou=groups", objectClasses = {"groupOfNames"})
public class LdapGroup {

    @Id
    private Name dn;

    @Attribute(name = "cn")
    @DnAttribute(value = "cn")
    private String name;

    @Attribute(name = "member")
    private Set<Name> members;

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Name> getMembers() {
        return members;
    }

    public void setMembers(Set<Name> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "LdapGroup{name='%s', members=%d}".formatted(name, members != null ? members.size() : 0);
    }
}
