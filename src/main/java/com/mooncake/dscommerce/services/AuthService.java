package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.entities.User;
import com.mooncake.dscommerce.services.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService (UserService userService) {
        this.userService = userService;
    }

    public void validateSelfOrAdmin(Long userId) {
        User loggedUser = userService.authenticated();
        if (!loggedUser.hasRole("ROLE_ADMIN") && !loggedUser.getId().equals(userId)) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
