package com.mooncake.dscommerce.mappers;

import com.mooncake.dscommerce.dto.UserDTO;
import com.mooncake.dscommerce.entities.Role;
import com.mooncake.dscommerce.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User entity);

    default String map(Role role) {
        return role == null ? null : role.getAuthority();
    }
}