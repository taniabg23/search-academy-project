package co.empathy.academy.search.user.service;

import co.empathy.academy.search.exceptions.RepeatedUserException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import co.empathy.academy.search.user.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    private ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    public List<User> getUsers() {
        List<User> allUsers = new LinkedList<>();
        users.forEach((id, user) -> allUsers.add(users.get(id)));
        return allUsers;
    }

    public User getUserById(Long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public User addUser(User user) throws RepeatedUserException {
        if (!this.users.containsKey(user.getId())) {
            this.users.put(user.getId(), user);
            return user;
        } else {
            throw new RepeatedUserException();
        }
    }

    public User deleteUser(Long id) throws UserNotFoundException {
        User user = this.users.remove(id);
        if (user == null) {
            throw new UserNotFoundException();
        } else {
            return user;
        }

    }

    public User updateUser(User user) throws UserNotFoundException {
        if (this.users.containsKey(user.getId())) {
            this.users.replace(user.getId(), user);
            return user;
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<User> saveUsers(MultipartFile file) throws IOException, RepeatedUserException {
        List<User> users = new ObjectMapper().readValue(file.getBytes(), new TypeReference<>() {
        });
        for (User user : users) {
            if (!this.users.containsKey(user.getId())) {
                this.users.put(user.getId(), user);
            } else {
                throw new RepeatedUserException();
            }
        }
        return users;
    }

    @Async
    public CompletableFuture<List<User>> saveUsersAsync(MultipartFile file) throws IOException, RepeatedUserException {
        List<User> users = new ObjectMapper().readValue(file.getBytes(), new TypeReference<>() {
        });
        for (User user : users) {
            if (!this.users.containsKey(user.getId())) {
                this.users.put(user.getId(), user);
            } else {
                throw new RepeatedUserException();
            }
        }
        return CompletableFuture.completedFuture(users);
    }
}
