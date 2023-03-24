package co.empathy.academy.search.user.controller;

import co.empathy.academy.search.exceptions.RepeatedUserException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import co.empathy.academy.search.user.model.User;
import co.empathy.academy.search.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("")
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
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserNotFoundException {
        User user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
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
    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User userR) throws RepeatedUserException {
        User user = this.userService.addUser(userR);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Create new users with the data stored in a json file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "A problem with the file has occurred")
    })
    @Parameter(name = "file", required = true, description = "File with the users' data")
    @PostMapping("/file")
    public ResponseEntity<List<User>> addUsersDataFile(@RequestParam MultipartFile file) {
        try {
            List<User> users = this.userService.saveUsers(file);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Create new users with the data stored in a json file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "A problem with the file has occurred")
    })
    @Parameter(name = "file", required = true, description = "File with the users' data")
    @PostMapping("/asyncfile")
    public ResponseEntity<CompletableFuture<List<User>>> addUsersDataFileAsync(@RequestParam MultipartFile file) {
        try {
            CompletableFuture<List<User>> users = this.userService.saveUsersAsync(file);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User userR) throws UserNotFoundException {
        User user = this.userService.updateUser(userR);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @Parameter(name = "id", required = true, description = "ID of the user you want to delete")
    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        User user = this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
