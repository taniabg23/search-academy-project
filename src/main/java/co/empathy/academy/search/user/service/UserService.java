package co.empathy.academy.search.user.service;

import co.empathy.academy.search.user.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    public List<User> getUsers() {
        List<User> allUsers = new LinkedList<>();
        users.forEach((id, user) -> allUsers.add(users.get(id)));
        return allUsers;
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public User addUser(User user) {
        return this.users.put(user.getId(), user);
    }

    public User deleteUser(Long id) {
        return this.users.remove(id);
    }

    public User updateUser(User user) {
        if (this.users.containsKey(user.getId())) {
            this.users.replace(user.getId(), user);
            return user;
        }
        return null;
    }

    public List<User> saveUsers(MultipartFile file) throws IOException {
        List<User> users = new ObjectMapper().readValue(file.getBytes(), new TypeReference<>() {
        });
        users.forEach(this::addUser);
        return users;
    }
}
