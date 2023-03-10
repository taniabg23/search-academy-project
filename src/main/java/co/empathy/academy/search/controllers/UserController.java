package co.empathy.academy.search.controllers;

import co.empathy.academy.search.model.User;
import co.empathy.academy.search.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/list")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("users/details/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users/add")
    public ResponseEntity<User> addUser(@RequestBody User userR) {
        User user = this.userService.addUser(userR);
        return user == null ?
                ResponseEntity.status(HttpStatus.CREATED).body(userR)
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/users/edit/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long idO, @RequestBody User userR) {
        User user = this.userService.updateUser(idO, userR);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = this.userService.deleteUser(id);
        return user == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
