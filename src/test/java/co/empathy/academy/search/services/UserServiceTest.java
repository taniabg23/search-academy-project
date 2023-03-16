package co.empathy.academy.search.services;

import co.empathy.academy.search.user.model.User;
import co.empathy.academy.search.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUP() {
        this.userService = new UserService();
    }

    @Test
    void givenNoUsers_whenAddNewUser_thenReturnsUserData() {
        User user = new User(1L, "user1", "user1@email");
        assertEquals(user, userService.addUser(user));
    }

    @Test
    void givenOneUser_whenAddUserRepeated_thenReturnsNull() {
        User user = new User(2L, "user2", "user2@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(null, userService.addUser(user));
    }

    @Test
    void givenNoUsers_whenGetUserById_thenReturnsNull() {
        assertEquals(null, userService.getUserById(3L));
    }

    @Test
    void givenOneUser_whenGetUserById_thenReturnsUsersData() {
        User user = new User(4L, "user4", "user4@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user, userService.getUserById(4L));
    }

    @Test
    void givenNoUser_whenDeleteUserThatDoesntExist_thenReturnsNull() {
        assertEquals(null, userService.deleteUser(5L));
    }

    @Test
    void givenOneUser_whenDeleteUser_thenReturnsUsersData() {
        User user = new User(5L, "user5", "user5@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user, userService.deleteUser(5L));
    }

    @Test
    void givenOneUser_whenUpdateUserExist_thenReturnsNewUser() {
        User user = new User(6L, "user6", "user6@email");
        User user2 = new User(6L, "user7", "user7@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user2, userService.updateUser(user2));
    }

    @Test
    void givenNoUser_whenUpdateUserDoesntExist_thenReturnsNull() {
        User user = new User(7L, "user7", "user7@email");
        assertEquals(null, userService.updateUser(user));
    }

    @Test
    void givenUsers_whenGetAllUsers_thenReturnsAllUsers() {
        User user = new User(8L, "user8", "user8@email");
        User user2 = new User(9L, "user9", "user9@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user2, userService.addUser(user2));
        assertTrue(userService.getUsers().contains(user));
        assertTrue(userService.getUsers().contains(user2));
    }
}
