package ru.otus.oop.exceptions;

import lombok.experimental.StandardException;

/**
 * Исключение, сигнализирующее, что превышен размер вместимости
 */
@StandardException
public class CapacityIsOverException extends RuntimeException {
}
