package ru.otus;

import ru.otus.annotations.tests.DemoTest1;
import ru.otus.annotations.tests.DemoTest2;
import ru.otus.annotations.tests.DemoTestBeforeFail;
import ru.otus.annotations.TestRunner;

import java.util.List;

/**
 * Основной класс для запуска демонстрации
 */
public class Application {
    public static void main(String[] args) {
        try {
            TestRunner.run(List.of(DemoTest1.class, DemoTest2.class, DemoTestBeforeFail.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
