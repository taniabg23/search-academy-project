package co.empathy.academy.search.controllers;

import co.empathy.academy.search.model.User;
import co.empathy.academy.search.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/users/list")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "Get a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @Parameter(name = "id", required = true, description = "ID of the user you want to get")
    @GetMapping("users/details/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "409", description = "Conflict")
    })
    @Parameters(value = {
            @Parameter(name = "id", required = true, description = "ID of the user you want to create"),
            @Parameter(name = "name", required = true, description = "Name of the user you want to create"),
            @Parameter(name = "email", required = true, description = "Email of the user you want to create")
    })
    @PostMapping("/users/add")
    public ResponseEntity<User> addUser(@RequestBody User userR) {
        User user = this.userService.addUser(userR);
        return user == null ?
                ResponseEntity.status(HttpStatus.CREATED).body(userR)
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Operation(summary = "Update user's info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @Parameters(value = {
            @Parameter(name = "idO", required = true, description = "ID of the user's info you want to edit"),
            @Parameter(name = "id", required = true, description = "New ID for the user "),
            @Parameter(name = "name", required = true, description = "New name for the user"),
            @Parameter(name = "email", required = true, description = "New email for the user")
    })
    @PutMapping("/users/edit/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long idO, @RequestBody User userR) {
        User user = this.userService.updateUser(idO, userR);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @Parameter(name = "id", required = true, description = "ID of the user you want to delete")
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = this.userService.deleteUser(id);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).build();
    }
}
