package co.empathy.academy.search.user.service;

import co.empathy.academy.search.exceptions.RepeatedUserException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import co.empathy.academy.search.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    public List<User> getUsers();

    public User getUserById(Long id) throws UserNotFoundException;

    public User addUser(User user) throws RepeatedUserException;

    public User deleteUser(Long id) throws UserNotFoundException;

    public User updateUser(User user) throws UserNotFoundException;

    public List<User> saveUsers(MultipartFile file) throws IOException, RepeatedUserException;

    public CompletableFuture<List<User>> saveUsersAsync(MultipartFile file) throws IOException, RepeatedUserException;
}
