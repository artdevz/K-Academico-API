package com.kacademico.interfaces.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademico.app.dto.role.RoleRequestDTO;
import com.kacademico.app.dto.role.RoleResponseDTO;
import com.kacademico.app.services.RoleService;
import com.kacademico.shared.utils.AsyncResultHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {
    
    private final RoleService roleS;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid RoleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncResultHandler.await(roleS.createAsync(request)));
    }



    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> read() {
        return ResponseEntity.ok(AsyncResultHandler.await(roleS.readAllAsync()));
    }


}
