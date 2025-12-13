package ru.yandex.practicum.exceptions;

public class TooShortWordException extends RuntimeException{
    public TooShortWordException(String message) {
        super(message);
    }
}
