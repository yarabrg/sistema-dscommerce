package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.entities.Role;
import com.mooncake.dscommerce.entities.User;
import com.mooncake.dscommerce.projections.UserDetailsProjection;
import com.mooncake.dscommerce.repositories.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    UserRepository repository;

    public UserService (UserRepository repository) {
        this.repository = repository;
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
}
