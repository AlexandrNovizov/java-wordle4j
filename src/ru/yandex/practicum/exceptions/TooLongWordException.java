package ru.yandex.practicum.exceptions;

public class TooLongWordException extends RuntimeException {
    public TooLongWordException(String message) {
        super(message);
    }
}
