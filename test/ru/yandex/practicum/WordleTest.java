package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    PrintWriter log;
    Random random;
    static final int DEFAULT_WORD_LENGTH = 5;
    static final int DEFAULT_STEPS_COUNT = 6;

    @BeforeEach
    public void init() {
        log = new PrintWriter(System.out);
        random = new Random(1L);
    }
    
    // WordleDictionary Tests

    @Test
    public void shouldReturnTrueForContainedValue() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);

        assertTrue(dictionary.contains("аврал"));
    }

    @Test
    public void shouldReturnFalseForNotContainedValue() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);

        assertFalse(dictionary.contains("раунд"));
    }

    @Test
    public void shouldThrowNoUnfilteredWordsExceptionIfAllWordsIsFiltered() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("c");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);

        dictionary.getAndRemoveRandomWord();
        dictionary.getAndRemoveRandomWord();

        assertThrows(NoUnfilteredWordsException.class, () -> dictionary.getAndRemoveRandomWord());
    }

    // WordleGame Tests
    
    @Test
    public void shouldThrowTooShortWordExceptionIfWordLengthLessThanGiven() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);

        assertThrows(TooShortWordException.class, () -> game.guess("араб"));
    }

    @Test
    public void shouldThrowTooLongWordExceptionIfWordLengthLessThanGiven() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);

        assertThrows(TooLongWordException.class, () -> game.guess("холодильник"));
    }

    @Test
    public void shouldThrowNotRussianWordExceptionIfWordContainsDigits() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);

        assertThrows(NotRussianWordException.class, () -> game.guess("1очка"));
    }

    @Test
    public void shouldThrowNotRussianWordExceptionIfWordContainsLatinLetters() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);

        assertThrows(NotRussianWordException.class, () -> game.guess("digit"));
    }

    @Test
    public void shouldThrowNotRussianWordExceptionIfWordContainsSpaces() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        String wordToGuess = "северный ветер";
        WordleGame game = new WordleGame(dictionary, log, wordToGuess.length());

        assertThrows(NotRussianWordException.class, () -> game.guess(wordToGuess));
    }

    @Test
    public void shouldThrowWordNotFoundInDictionaryExceptionIfGivenWordNotInDictionary() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);

        assertThrows(WordNotFoundInDictionaryException.class, () -> game.guess("нахал"));
    }

    @Test
    public void shouldThrowWordAlreadyGuessedExceptionIfGivenWordAlreadyGuessed() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);
        game.setAnswer("раунд");
        String wordToGuess = "аврал";

        game.guess(wordToGuess);
        assertThrows(WordAlreadyGuessedException.class, () -> game.guess(wordToGuess));
    }

    @Test
    public void shouldStopGameIfWordGuessed() {
        ArrayList<String> words = new ArrayList<>();
        words.add("аврал");
        words.add("раунд");
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);
        String wordToGuess = "аврал";
        game.setAnswer(wordToGuess);

        game.guess(wordToGuess);

        assertFalse(game.isRunning());
    }

    @Test
    public void shouldThrowStepsLimitExceededExceptionIfRemainingStepsCountIs0() {
        ArrayList<String> words = new ArrayList<>();
        char[] chars = {'ф', 'ы', 'в', 'а', 'о', 'л', 'д'};
        for (char ch : chars) {
            words.add(String.valueOf(ch).repeat(DEFAULT_WORD_LENGTH));
        }
        WordleDictionary dictionary = new WordleDictionary(words, random, log);
        WordleGame game = new WordleGame(dictionary, log, DEFAULT_WORD_LENGTH);
        game.setAnswer("д".repeat(DEFAULT_WORD_LENGTH));

        for (int i = 0; i < DEFAULT_STEPS_COUNT - 1; i++) {
            game.guess(String.valueOf(chars[i]).repeat(DEFAULT_WORD_LENGTH));
        }

        assertThrows(StepsLimitExceededException.class, () -> game.guess("л".repeat(DEFAULT_WORD_LENGTH)));
    }
}
