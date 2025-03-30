package com.kacademic.interfaces.controllers;

// import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.dto.role.RoleRequestDTO;
import com.kacademic.app.services.InitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/init")
@RestController
public class InitController {

    private final InitService initS;

    public InitController(InitService initS) {
        this.initS = initS;
    }

    @Operation(
        summary = "Create an Admin",
        description = "Create a new user with admin role"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Admin successfully created"),
        @ApiResponse(responseCode = "409", description = "Admin already be created")
    })
    @PostMapping("/admin")
    public ResponseEntity<String> initAdmin() {
        // return initS.initAdminAsync().thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
        return new ResponseEntity<>(initS.initAdminAsync(), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Create a Role",
        description = "Create a new role with UNIQUE name and a description"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role succesfully created"),
        @ApiResponse(responseCode = "409", description = "Role name already being used")
    })
    @PostMapping("/roles")
    public ResponseEntity<String> initRoles(@RequestBody @Valid RoleRequestDTO data) {
        // return initS.initRolesAsync(data).thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
        return new ResponseEntity<>(initS.initRolesAsync(data), HttpStatus.CREATED);
    }
    
}
