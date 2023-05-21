package ru.otus.jdbc.mapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * простая реализация кэша
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class SimpleCache<K, V> implements Cache<K, V> {
    private final Map<K, V> map;

    public SimpleCache() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V computeIfAbsent(K key, Function<K, V> function) {
        return map.computeIfAbsent(key, function);
    }
}
