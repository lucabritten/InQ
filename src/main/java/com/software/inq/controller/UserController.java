package com.software.inq.controller;

import com.software.inq.dto.UserDTO;
import com.software.inq.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name= "Users", description = "User Management API")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "get all Users")
    public ResponseEntity<List<UserDTO>> getAll(){
        List<UserDTO> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a specific User by id")
    public ResponseEntity<UserDTO> getOne(@PathVariable Long id){
        UserDTO user = userService.getOne(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "create a new User")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO){
        UserDTO createdUser = userService.create(userDTO);
        return ResponseEntity
                .created(URI.create("/api/users/" + createdUser.id()))
                .body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a specific User by id")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        UserDTO updatedUser = userService.update(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a specific User by id")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();

    }
}
