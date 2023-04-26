package co.empathy.academy.search.user.service;

import co.empathy.academy.search.exceptions.RepeatedUserException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import co.empathy.academy.search.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    /**
     * Method that lists all the users
     *
     * @return a list with all the users
     */
    List<User> getUsers();

    /**
     * Method that returns the user with the parameter id
     *
     * @param id the id of the user we are looking for
     * @return the user with the parameter id
     * @throws UserNotFoundException when there's no user with the parameter id
     */
    User getUserById(Long id) throws UserNotFoundException;

    /**
     * Method that adds the Parameter user
     *
     * @param user the user we want to add
     * @return the added user
     * @throws RepeatedUserException when there's already an user with the same id
     */
    User addUser(User user) throws RepeatedUserException;

    /**
     * Method that deletes the user with the parameter id
     *
     * @param id the id of the user we want to delete
     * @return the user we have deleted
     * @throws UserNotFoundException when there's no user with the parameter id
     */
    User deleteUser(Long id) throws UserNotFoundException;

    /**
     * Method that updates the user that has tha same id as the parameter user with this user information
     *
     * @param user user information we want to update
     * @return the user with the new information
     * @throws UserNotFoundException when there's no user with the parameter id
     */
    User updateUser(User user) throws UserNotFoundException;

    /**
     * Method that adds new users taking the information from the parameter file
     *
     * @param file with the user's information
     * @return the list of the users created with the parmaeter file information
     * @throws IOException
     * @throws RepeatedUserException when there's already a user with the same id
     */
    List<User> saveUsers(MultipartFile file) throws IOException, RepeatedUserException;

    /**
     * Method that adds asynchronous new users taking the information from the parameter file
     *
     * @param file with the user's information
     * @return a CompletableFuture object with the list of the users created with the parmaeter file information
     * @throws IOException
     * @throws RepeatedUserException when there's already a user with the same id
     */
    CompletableFuture<List<User>> saveUsersAsync(MultipartFile file) throws IOException, RepeatedUserException;
}
