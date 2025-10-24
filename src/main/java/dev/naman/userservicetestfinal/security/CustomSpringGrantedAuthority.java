package dev.naman.userservicetestfinal.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.naman.userservicetestfinal.models.Role;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize(as = CustomSpringGrantedAuthority.class)
public record CustomSpringGrantedAuthority(Role role) implements GrantedAuthority {

    @Override
    @JsonIgnore
    public String getAuthority() {
        return role.getRole();
    }
}
