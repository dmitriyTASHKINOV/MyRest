package ru.kata.spring.boot_security.demo.exceptions;

public class IncorrectUserDataException extends RuntimeException{
    public IncorrectUserDataException(String message) {
        super(message);
    }
}
