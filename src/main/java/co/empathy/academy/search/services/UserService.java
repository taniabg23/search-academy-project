package co.empathy.academy.search.services;

import co.empathy.academy.search.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private ConcurrentHashMap<String, User> users;

    //TODO comprobar que el user y id existen
    public void addUser(User user) {
        this.users.put(user.getId(), user);
    }

    //TODO comprobar si existe el usuario y el id
    public void deleteUser(User user) {
        this.users.remove(user.getId());
    }

    //TODO comprobar si existe el usuario y el id
    public void updateUser(User user) {
        this.users.replace(user.getId(), user);
    }
}
