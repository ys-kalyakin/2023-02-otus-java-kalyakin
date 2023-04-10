package ru.otus.bytecode.proxy;

import ru.otus.bytecode.annotation.Log;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public enum LogCache {
    INSTANCE;

    private final Map<Class<?>, Map<MethodData, String[]>> cache = new ConcurrentHashMap<>();

    /**
     * выполнить кэширование данных методов c аннотацией {@link Log}
     *
     * @param clazz класс
     */
    public Map<MethodData, String[]> makeCacheForClass(Class<?> clazz) {
        return cache.computeIfAbsent(
                clazz,
                c -> Arrays.stream(clazz.getDeclaredMethods())
                        .filter(m -> m.isAnnotationPresent(Log.class))
                        .map(m -> Map.entry(new MethodData(m.getName(),
                                        m.getParameterCount(),
                                        m.getGenericParameterTypes()),
                                ArgumentNameExtractor.getParameterNames(m))
                        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
