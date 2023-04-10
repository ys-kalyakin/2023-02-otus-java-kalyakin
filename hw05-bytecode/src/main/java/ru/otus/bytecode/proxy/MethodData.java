package ru.otus.bytecode.proxy;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * Данные метода
 *
 * @param name название
 * @param parameterCount количество параметров
 * @param types типы параметров
 */
public record MethodData(String name, int parameterCount, Type[] types) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodData that = (MethodData) o;
        return parameterCount == that.parameterCount && Objects.equals(name, that.name) && Arrays.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, parameterCount);
        result = 31 * result + Arrays.hashCode(types);
        return result;
    }
}
