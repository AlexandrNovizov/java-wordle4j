package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class WordleDictionaryLoader {

    public final int wordLength;

    private final String filename;

    private final PrintWriter log;

    public WordleDictionaryLoader(String filename, int wordLength, PrintWriter log) {
        this.filename = filename;
        this.wordLength = wordLength;
        this.log = log;
    }

    public WordleDictionary load() throws IOException, DictionaryNotFoundException {
        log.println("Загрузка словаря " + filename);
        List<String> words = new LinkedList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String word = reader.readLine();
                if (word.length() == wordLength) {
                    word = word.toLowerCase();
                    while (word.indexOf('ё') != -1) {
                        word = word.replace('ё', 'е');
                    }
                    words.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            throw new DictionaryNotFoundException(e.getMessage());
        }
        log.println("Загрузка завершена");
        return new WordleDictionary(words, log);
    }
}
