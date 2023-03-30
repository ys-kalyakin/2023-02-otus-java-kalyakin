package ru.otus.annotations;

import java.lang.annotation.*;

/**
 * Аннотация дла метода класса, которая маркирует unit-тесты
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
