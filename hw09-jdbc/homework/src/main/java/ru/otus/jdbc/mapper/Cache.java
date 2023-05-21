package ru.otus.jdbc.mapper;

import java.util.function.Function;

/**
 * Кэш
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public interface Cache<K, V> {
    /**
     * получить значение по ключу
     *
     * @param key ключ
     * @return значени
     */
    V get(K key);

    /**
     * сохрание значение в кэш
     *
     * @param key ключ
     * @param value значение
     */
    void put(K key, V value);

    /**
     * вычислить значение, если его ещё нет в кэше
     *
     * @param key ключ
     * @param function функция вычисления значения
     *
     * @return значениe
     */
    V computeIfAbsent(K key, Function<K, V> function);
}
