package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public final int WORD_LENGTH;

    private final String filename;

    private final PrintWriter log;

    public WordleDictionaryLoader(String filename, int wordLength, PrintWriter log) {
        this.filename = filename;
        WORD_LENGTH = wordLength;
        this.log = log;
    }

    public WordleDictionary load() throws IOException, DictionaryNotFoundException {
        log.println("Загрузка словаря " + filename);
        List<String> words = new LinkedList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String word = reader.readLine();
                if (word.length() == WORD_LENGTH) {
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
        return new WordleDictionary(words, WORD_LENGTH, log);
    }
}
