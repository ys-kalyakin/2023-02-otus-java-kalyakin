package ru.otus.bytecode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Обработчик логгирования параметров
 */
public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Optional.ofNullable(LogCache.INSTANCE.makeCacheForClass(target.getClass())
                        .get(new MethodData(method.getName(), method.getParameterCount(), method.getGenericParameterTypes())))
                        .ifPresent(names -> log(method.getName(), names, args));
        return method.invoke(target, args);
    }

    private void log(String method, String [] parameterNames, Object[] args) {
        System.out.printf("Method %s call\n", method);
        for (int i = 0; i < parameterNames.length; ++i) {
            System.out.printf("%s -> %s\n", parameterNames[i], args[i]);
        }
    }
}
