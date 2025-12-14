package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    private boolean isRunning;

    private boolean isFromHelper = false;

    private final PrintWriter log;

    private final Set<String> guessedWords = new HashSet<>();
    private final int WORD_LENGTH;

    public WordleGame(WordleDictionary dictionary, PrintWriter log, int wordLength) {
        this.dictionary = dictionary;
        steps = 6;
        this.log = log;
        WORD_LENGTH = wordLength;
        isRunning = true;
        answer = dictionary.getWord(false);
        this.log.println("Загадано слово " + answer);
    }

    public String getRandomWord() {
        isFromHelper = true;
        return dictionary.getWord(true);
    }

    public String guess(String word) {
        log.println("Введено слово '" + word + "'");

        checkWord(word);

        if (answer.equals(word)) {
            log.println("Победа игрока");
            isRunning = false;
            return answer;
        }

        steps--;
        if (steps == 0) {
            isRunning = false;
            throw new StepsLimitExceededException("Попытки закончились!");
        }
        log.println("Осталось " + steps + " попыток");
        guessedWords.add(word);
        String mask = getMask(word);
        log.println("Выведено " + mask);
        dictionary.filter(word, mask);
        return mask;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private void checkWord(String word) {
        if (word.length() < WORD_LENGTH && !word.isEmpty()) {
            String message = "Слово '" + word + "' слишком короткое";
            log.println(message);
            throw new TooShortWordException(message);
        } else if (word.length() > WORD_LENGTH) {
            String message = "Слово '" + word + "' слишком длинное";
            log.println(message);
            throw new TooLongWordException(message);
        }

        if (!word.matches("[а-яА-Я]+")) {
            String message = "Слово '" + word + "' не является русским";
            log.println(message);
            throw new NotRussianWordException(message);
        }

        if (!(isFromHelper || dictionary.contains(word))) {
            isFromHelper = false;
            String message = "Слово '" + word + "' не найдено в словаре";
            log.println(message);
            throw new WordNotFoundInDictionaryException(message);
        }

        if (guessedWords.contains(word)) {
            throw new WordAlreadyGuessedException("Слово '" + word + "' уже было загадано");
        }
    }

    private String getMask(String word) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; ++i) {
            char ch = word.charAt(i);
            if (answer.indexOf(ch) != -1) {
                if (answer.charAt(i) == ch) {
                    builder.append(SpecialCharacter.HAS.getCharacter());
                } else {
                    builder.append(SpecialCharacter.CONTAINS.getCharacter());
                }
            } else {
                builder.append(SpecialCharacter.NOT_CONTAINS.getCharacter());
            }
        }
        return builder.toString();
    }
}
