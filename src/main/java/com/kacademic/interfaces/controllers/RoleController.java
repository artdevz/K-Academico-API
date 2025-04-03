package com.kacademic.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.dto.role.RoleRequestDTO;
import com.kacademic.app.services.RoleService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RequestMapping("/role")
@RestController
public class RoleController {
    
    private final RoleService roleS;

    public RoleController(RoleService roleS) {
        this.roleS = roleS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid RoleRequestDTO request) {
        return new ResponseEntity<>(roleS.createAsync(request), HttpStatus.CREATED);
    }

}
