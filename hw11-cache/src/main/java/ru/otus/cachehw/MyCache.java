package ru.otus.cachehw;


import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> map;
    private final List<HwListener<K, V>> listeners;

    public MyCache() {
        this.map = new WeakHashMap<>();
        this.listeners = new LinkedList<>();
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
        listeners.forEach(l -> l.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        var value = map.remove(key);
        listeners.forEach(l -> l.notify(key, value, "remove"));
    }

    @Override
    public V get(K key) {
        var value = map.get(key);
        listeners.forEach(l -> l.notify(key, value, "get"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(kvHwListener -> listener == kvHwListener);
    }
}
