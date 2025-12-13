package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;


public class WordleDictionary {

    private final Set<String> words;

    private final Set<FilterRule> appliedFilters = new HashSet<>();

    private final PrintWriter log;
    private final int WORD_LENGTH;

    public WordleDictionary(Collection<String> words, int wordLength, PrintWriter log) {
        this.words = new HashSet<>(words);
        this.log = log;
        WORD_LENGTH = wordLength;
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getAndRemoveWord() {
        String word = getWord();
        words.remove(word);
        log.println("Удалено случайное слово " + word);
        return word;
    }

    public String getWord() {
        int index = new Random().nextInt(Math.min(WORD_LENGTH, words.size()));
        String word = words.toArray()[index].toString();
        log.println("Подобрано случайное слово " + word);
        return word;
    }

    public void filter(String word, String mask) {
        for (int i = 0; i < mask.length(); i++) {
            SpecialCharacter ch = SpecialCharacter.fromChar(mask.charAt(i));
            FilterRule rule;
            if (ch == SpecialCharacter.NOT_CONTAINS) {
                rule = new FilterRule(ch, word.charAt(i), mask.length());
            } else {
                rule = new FilterRule(ch, word.charAt(i), i);
            }
            if (!appliedFilters.contains(rule)) {
                appliedFilters.add(rule);
                switch (rule.rule) {
                    case CONTAINS -> {
                        removeIfCharAtIndex(word.charAt(i), i);
                        removeIfNotContains(word.charAt(i));
                    }
                    case HAS -> removeIfCharNotAtIndex(word.charAt(i), i);
                    case NOT_CONTAINS -> removeIfContains(word.charAt(i));
                }
            }
        }
    }

    private void removeIfCharAtIndex(char ch, int index) {
        words.removeIf(word -> word.charAt(index) == ch);
    }

    private void removeIfCharNotAtIndex(char ch, int index) {
        words.removeIf(word -> word.charAt(index) != ch);
    }

    private void removeIfContains(char ch) {
        words.removeIf(word -> word.indexOf(ch) != -1);
    }

    private void removeIfNotContains(char ch) {words.removeIf(word -> word.indexOf(ch) == -1);}

    private static class FilterRule {

        private final SpecialCharacter rule;
        private final char character;
        private final int index;

        public FilterRule(SpecialCharacter rule, char character, int index) {
            this.rule = rule;
            this.character = character;
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            FilterRule that = (FilterRule) o;
            return index == that.index && character == that.character && Objects.equals(rule, that.rule);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rule, character, index);
        }
    }
}
