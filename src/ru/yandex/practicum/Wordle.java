package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static Path LOG_FILE = Path.of("log.txt");
    public static Path DICTIONARY_FILE = Path.of("words_ru.txt");
    public static final int WORD_LENGTH = 5;

    public static void main(String[] args) {

        try(PrintWriter log = createLog(); Scanner scanner = new Scanner(System.in)) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(DICTIONARY_FILE.toString(), WORD_LENGTH, log);
            WordleDictionary dictionary = loader.load();
            WordleGame game = new WordleGame(dictionary, log, WORD_LENGTH);
            System.out.println("Игра началась! Угадайте русское слово из " + WORD_LENGTH + " букв");
            while (game.isRunning()) {
                try {
                    System.out.print("-> ");
                    String guess = scanner.nextLine();
                    if (guess.isBlank()) {
                        guess = game.getRandomWord();
                        System.out.println("-> Подсказка: " + guess);
                    }
                    String response = game.guess(guess);
                    if (!game.isRunning()) {
                        System.out.println("Поздравляем! Вы угадали слово " + response + "!");
                    } else {
                        System.out.println("-> " + response);
                    }
                } catch (StepsLimitExceededException e) {
                    System.out.println(e.getMessage() + " Загаданное слово: " + game.getAnswer());
                } catch (TooLongWordException | TooShortWordException e) {
                    System.out.println("Длина слова должна быть равна " + WORD_LENGTH);
                } catch (NotRussianWordException e) {
                    System.out.println("Слово должно быть русским!");
                } catch (WordNotFoundInDictionaryException e) {
                    System.out.println("Такого слова в словаре нет!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static PrintWriter createLog() throws IOException {
        if (Files.exists(LOG_FILE)) {
            Files.delete(LOG_FILE);
        }

        Files.createFile(LOG_FILE);

        return new PrintWriter(LOG_FILE.toFile());
    }

}
