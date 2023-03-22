package ru.otus.annotations;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Утилитный методы для тестов
 */
public final class Asserts {
    private Asserts() {}

    public static void equals(Object expected, Object current) {
        if (!Objects.equals(expected, current))
            throw new AssertionErrorException(MessageFormat.format("Ожидаемое значение {0}, текущее значение {1}", expected.toString(), current.toString()));
    }
}
