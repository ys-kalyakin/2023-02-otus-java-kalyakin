package ru.otus.bytecode;

import ru.otus.bytecode.annotation.Log;
import ru.otus.bytecode.proxy.LoggingInvocationHandler;

public class TestLogging implements TestLoggingInterface {
    private TestLogging() {}

    /**
     * @return новый инстанс класса
     */
    public static TestLoggingInterface newInstance() {
        return LoggingInvocationHandler.wrap(TestLoggingInterface.class, new TestLogging());
    }

    @Override
    public void calculation(int param1)  {
        // calculation
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        // calculation
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        // calculation
    }
}
