package co.empathy.academy.search.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }
}
