package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;


public class WordleDictionary {

    private final Set<String> words;
    private final List<String> filteredWords;

    private final Set<FilterRule> appliedFilters = new HashSet<>();

    private final PrintWriter log;

    public WordleDictionary(Collection<String> words, PrintWriter log) {
        this.words = new HashSet<>(words);
        filteredWords = new LinkedList<>(words);
        this.log = log;
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getAndRemoveWord() {
        String word = getWord();
        filteredWords.remove(word);
        log.println("Удалено случайное слово " + word);
        return word;
    }

    public String getWord() {
        int index = new Random().nextInt(filteredWords.size());
        String word = filteredWords.get(index);
        log.println("Подобрано случайное слово " + word);
        return word;
    }

    public void filter(String word, String mask) {
        for (int i = 0; i < mask.length(); i++) {
            SpecialCharacter ch = SpecialCharacter.fromChar(mask.charAt(i));
            FilterRule rule;
            if (ch == SpecialCharacter.NOT_CONTAINS) {
                // если слово не содержит букву, то не важно, где она стоит, и индекс = -1
                rule = new FilterRule(ch, word.charAt(i), -1);
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
        filteredWords.removeIf(word -> word.charAt(index) == ch);
    }

    private void removeIfCharNotAtIndex(char ch, int index) {
        filteredWords.removeIf(word -> word.charAt(index) != ch);
    }

    private void removeIfContains(char ch) {
        filteredWords.removeIf(word -> word.indexOf(ch) != -1);
    }

    private void removeIfNotContains(char ch) {filteredWords.removeIf(word -> word.indexOf(ch) == -1);}

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
