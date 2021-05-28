package ua.project.forit.data.entities.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @NotBlank
    protected String name;

    @ManyToMany(mappedBy = "authorities")
    protected Set<User> users;

    {
        users = new HashSet<>();
    }

    public Authority() {}

    public Authority(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Authority))
            return false;

        Authority authority = (Authority) object;
        return (object == this || this.name.equals(authority.getAuthority()));
    }

    @Override
    public String getAuthority()
    {
        return name;
    }
}
