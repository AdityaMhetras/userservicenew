package dev.naman.userservicetestfinal.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO for {@link dev.naman.userservicetestfinal.models.Role}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RoleDto(Long id, String role) implements Serializable {
}