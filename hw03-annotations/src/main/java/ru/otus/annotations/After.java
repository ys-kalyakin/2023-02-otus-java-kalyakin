package ru.otus.annotations;

import java.lang.annotation.*;

/**
 * Аннотация для метода класса, помечающая метод, который должен быть выполнен после теста
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface After {
}
