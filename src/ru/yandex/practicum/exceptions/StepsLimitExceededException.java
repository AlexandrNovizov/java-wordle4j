package ru.yandex.practicum.exceptions;

public class StepsLimitExceededException extends RuntimeException {
    public StepsLimitExceededException(String message) {
        super(message);
    }
}
