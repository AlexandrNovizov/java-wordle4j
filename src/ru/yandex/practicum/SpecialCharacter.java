package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.IllegalSpecialCharacterException;

public enum SpecialCharacter {
    CONTAINS('^'),
    HAS('+'),
    NOT_CONTAINS('-');

    private final char character;

    SpecialCharacter(char character) {
        this.character = character;
    }

    public static SpecialCharacter fromChar(char ch) {
        return switch (ch) {
            case '+' -> SpecialCharacter.HAS;
            case '-' -> SpecialCharacter.NOT_CONTAINS;
            case '^' -> SpecialCharacter.CONTAINS;
            default -> throw new IllegalSpecialCharacterException("Неожиданный символ '" + ch + "'");
        };
    }

    public char getCharacter() {
        return character;
    }
}
