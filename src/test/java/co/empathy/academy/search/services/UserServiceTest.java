package co.empathy.academy.search.services;

import co.empathy.academy.search.exceptions.RepeatedUserException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import co.empathy.academy.search.user.model.User;
import co.empathy.academy.search.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUP() {
        this.userService = new UserServiceImpl();
    }

    @Test
    void givenNoUsers_whenAddNewUser_thenReturnsUserData() throws RepeatedUserException {
        User user = new User(1L, "user1", "user1@email");
        assertEquals(user, userService.addUser(user));
    }

    @Test
    void givenOneUser_whenAddUserRepeated_thenThrowsRepeatedUserException() throws RepeatedUserException {
        User user = new User(2L, "user2", "user2@email");
        assertEquals(user, userService.addUser(user));
        assertThrows(RepeatedUserException.class, () -> userService.addUser(user));
    }

    @Test
    void givenNoUsers_whenGetUserById_thenThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(3L));
    }

    @Test
    void givenOneUser_whenGetUserById_thenReturnsUsersData() throws RepeatedUserException, UserNotFoundException {
        User user = new User(4L, "user4", "user4@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user, userService.getUserById(4L));
    }

    @Test
    void givenNoUser_whenDeleteUserThatDoesntExist_thenThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(5L));
    }

    @Test
    void givenOneUser_whenDeleteUser_thenReturnsUsersData() throws RepeatedUserException, UserNotFoundException {
        User user = new User(5L, "user5", "user5@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user, userService.deleteUser(5L));
    }

    @Test
    void givenOneUser_whenUpdateUserExist_thenReturnsNewUser() throws RepeatedUserException, UserNotFoundException {
        User user = new User(6L, "user6", "user6@email");
        User user2 = new User(6L, "user7", "user7@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user2, userService.updateUser(user2));
    }

    @Test
    void givenNoUser_whenUpdateUserDoesntExist_thenReturnsUserNotFoundException() {
        User user = new User(7L, "user7", "user7@email");
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void givenUsers_whenGetAllUsers_thenReturnsAllUsers() throws RepeatedUserException {
        User user = new User(8L, "user8", "user8@email");
        User user2 = new User(9L, "user9", "user9@email");
        assertEquals(user, userService.addUser(user));
        assertEquals(user2, userService.addUser(user2));
        assertTrue(userService.getUsers().contains(user));
        assertTrue(userService.getUsers().contains(user2));
    }
}
