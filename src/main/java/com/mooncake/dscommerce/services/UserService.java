package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.ProductDTO;
import com.mooncake.dscommerce.dto.UserDTO;
import com.mooncake.dscommerce.entities.Role;
import com.mooncake.dscommerce.entities.User;
import com.mooncake.dscommerce.projections.UserDetailsProjection;
import com.mooncake.dscommerce.repositories.UserRepository;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public UserService (UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("E-mail not found");
        }

        User user = new User();
        user.setEmail(result.getFirst().getUsername());
        user.setPassword(result.getFirst().getPassword());
        for(UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    protected User authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwtPrincipal)) {
            throw new UsernameNotFoundException("User not authenticated");
        }

        String username = jwtPrincipal.getClaim("username");

        if (username == null) {
            throw new UsernameNotFoundException("Username claim not found in token");
        }

        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("E-mail not found"));
    }

    @Transactional(readOnly = true)
    public UserDTO getLoggedUser() {
        User user = authenticated();
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.setRoles(user.getRoles().stream().map(Role::getAuthority).toList());

        return dto;
    }
}
