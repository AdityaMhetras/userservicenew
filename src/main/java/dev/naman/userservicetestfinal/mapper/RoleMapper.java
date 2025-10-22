package dev.naman.userservicetestfinal.mapper;

import dev.naman.userservicetestfinal.dtos.RoleDto;
import dev.naman.userservicetestfinal.models.Role;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);

    String map(Role value);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleDto roleDto, @MappingTarget Role role);
}