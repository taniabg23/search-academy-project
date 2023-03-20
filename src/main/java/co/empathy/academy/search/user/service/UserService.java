package co.empathy.academy.search.user.service;

import co.empathy.academy.search.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public List<User> getUsers();

    public User getUserById(Long id);

    public User addUser(User user);

    public User deleteUser(Long id);

    public User updateUser(User user);

    public List<User> saveUsers(MultipartFile file) throws IOException;
}
