package co.empathy.academy.search.user.controller;

import co.empathy.academy.search.user.model.User;
import co.empathy.academy.search.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User userR) {
        User user = this.userService.addUser(userR);
        return user == null ?
                ResponseEntity.status(HttpStatus.CONFLICT).build()
                : ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/users/addfile")
    public ResponseEntity<List<User>> addUsersDataFile(@RequestParam MultipartFile file) {
        try {
            List<User> users = this.userService.saveUsers(file);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User userR) {
        User user = this.userService.updateUser(userR);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = this.userService.deleteUser(id);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
