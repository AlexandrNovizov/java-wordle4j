package ru.yandex.practicum.exceptions;

public class NoUnfilteredWordsException extends RuntimeException {
    public NoUnfilteredWordsException(String message) {
        super(message);
    }
}
