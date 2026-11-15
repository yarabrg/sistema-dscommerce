package com.mooncake.dscommerce.controllers;

import com.mooncake.dscommerce.dto.UserDTO;
import com.mooncake.dscommerce.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService service;

    public UserController (UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping(value = "/profile")
    public ResponseEntity<UserDTO> getLoggedUser() {
        UserDTO dto = service.getLoggedUser();
        return ResponseEntity.ok(dto);
    }
}
