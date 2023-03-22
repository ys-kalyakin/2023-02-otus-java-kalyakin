package ru.otus.annotations;

/**
 * Ошибка сравнения
 */
public class AssertionErrorException extends RuntimeException {
    public AssertionErrorException() {
    }

    public AssertionErrorException(String message) {
        super(message);
    }

    public AssertionErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertionErrorException(Throwable cause) {
        super(cause);
    }

    public AssertionErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
