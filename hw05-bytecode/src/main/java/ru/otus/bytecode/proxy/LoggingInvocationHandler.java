package ru.otus.bytecode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Обработчик логгирования параметров
 */
public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final LogCache logCache;

    public LoggingInvocationHandler(Object target, LogCache logCache) {
        this.target = target;
        this.logCache = logCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logCache.getParameterNames(
                target.getClass(),
                new MethodData(method.getName(), method.getParameterCount(), method.getGenericParameterTypes())
        ).ifPresent(names -> log(method.getName(), names, args));
        return method.invoke(target, args);
    }

    private void log(String method, String [] parameterNames, Object[] args) {
        System.out.printf("Method %s call\n", method);
        for (int i = 0; i < parameterNames.length; ++i) {
            System.out.printf("%s -> %s\n", parameterNames[i], args[i]);
        }
    }
}
