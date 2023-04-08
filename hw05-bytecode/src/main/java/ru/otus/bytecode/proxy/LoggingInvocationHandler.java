package ru.otus.bytecode.proxy;

import ru.otus.bytecode.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Обработчик логгирования параметров
 */
public class LoggingInvocationHandler implements InvocationHandler {
    private static final Map<Class<?>, Map<MethodData, String[]>> CACHE = new ConcurrentHashMap<>();
    private final Object target;

    private LoggingInvocationHandler(Object target) {
        this.target = target;
        CACHE.computeIfAbsent(
                target.getClass(),
                c -> Arrays.stream(target.getClass().getDeclaredMethods())
                        .filter(m -> m.isAnnotationPresent(Log.class))
                        .map(m -> Map.entry(new MethodData(m.getName(),
                                m.getParameterCount(),
                                m.getGenericParameterTypes()),
                                ArgumentNameExtractor.getParameterNames(m))
                        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    /**
     * обернуть объект в прокси
     *
     * @param iface интерфейс
     * @param object объект класса
     * @return прокси
     * @param <T> тип интерфейса
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(Class<T> iface, T object) {
        var invocationHandler = new LoggingInvocationHandler(object);
        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                new Class<?>[] {iface},
                invocationHandler
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Optional.ofNullable(CACHE.getOrDefault(target.getClass(), Collections.emptyMap())
                .get(new MethodData(method.getName(), method.getParameterCount(), method.getGenericParameterTypes())))
                .ifPresent(names -> {
                            System.out.printf("Method %s call\n", method);
                            for (int i = 0; i < method.getParameterCount(); ++i) {
                                System.out.printf("%s -> %s\n", names[i], args[i]);
                            }
                        }
                );
        return method.invoke(target, args);
    }

    private record MethodData(String name, int parameterCount, Type[] types) {
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
}
