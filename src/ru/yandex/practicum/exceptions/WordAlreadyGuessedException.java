package ru.yandex.practicum.exceptions;

public class WordAlreadyGuessedException extends RuntimeException {
    public WordAlreadyGuessedException(String message) {
        super(message);
    }
}
