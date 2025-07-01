package org.example.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authority_roles")
public class AuthorityRole {
    @EmbeddedId
    private AuthorityRoleId id;

    @MapsId("authorityid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorityid", nullable = false)
    private Authority authorityid;

    @MapsId("roleid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roleid", nullable = false)
    private Role roleid;

    public AuthorityRoleId getId() {
        return id;
    }

    public void setId(AuthorityRoleId id) {
        this.id = id;
    }

    public Authority getAuthorityid() {
        return authorityid;
    }

    public void setAuthorityid(Authority authorityid) {
        this.authorityid = authorityid;
    }

    public Role getRoleid() {
        return roleid;
    }

    public void setRoleid(Role roleid) {
        this.roleid = roleid;
    }

}