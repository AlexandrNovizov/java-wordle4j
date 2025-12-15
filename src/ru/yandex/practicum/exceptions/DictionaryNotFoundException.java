package ru.yandex.practicum.exceptions;

public class DictionaryNotFoundException extends RuntimeException {
  public DictionaryNotFoundException(String message) {
    super(message);
  }
}
