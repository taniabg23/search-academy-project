package co.empathy.academy.search.exceptions;

public class RepeatedUserException extends Exception {

    public RepeatedUserException() {
        super("User already exists");
    }
}
