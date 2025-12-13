package ru.yandex.practicum.exceptions;

public class NotRussianWordException extends RuntimeException {
    public NotRussianWordException(String message) {
        super(message);
    }
}
