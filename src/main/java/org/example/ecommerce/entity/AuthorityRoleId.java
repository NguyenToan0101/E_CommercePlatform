package org.example.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthorityRoleId implements Serializable {
    private static final long serialVersionUID = 5078624661492883980L;
    @NotNull
    @Column(name = "authorityid", nullable = false)
    private Integer authorityid;

    @NotNull
    @Column(name = "roleid", nullable = false)
    private Integer roleid;

    public Integer getAuthorityid() {
        return authorityid;
    }

    public void setAuthorityid(Integer authorityid) {
        this.authorityid = authorityid;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthorityRoleId entity = (AuthorityRoleId) o;
        return Objects.equals(this.authorityid, entity.authorityid) &&
                Objects.equals(this.roleid, entity.roleid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityid, roleid);
    }

}