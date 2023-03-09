package co.empathy.academy.search.services;

import co.empathy.academy.search.model.User;
import org.springframework.stereotype.Service;

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

    //TODO comprobar datos
    public User getUserById(Long id) {
        return users.get(id);
    }

    //TODO comprobar que el user y id existen
    public User addUser(User user) {
        return this.users.put(user.getId(), user);
    }

    //TODO comprobar si existe el usuario y el id
    public User deleteUser(Long id) {
        return this.users.remove(id);
    }

    //TODO comprobar si existe el usuario y el id
    public User updateUser(Long id, User user) {
        return this.users.replace(id, user);
    }
}
