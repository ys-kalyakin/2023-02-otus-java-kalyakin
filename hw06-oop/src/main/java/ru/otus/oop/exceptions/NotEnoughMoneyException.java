package ru.otus.oop.exceptions;

import lombok.experimental.StandardException;

/**
 * Исключение, сигнализирующее о том, что в банкомате недостаточно средств
 */
@StandardException
public class NotEnoughMoneyException extends RuntimeException {
}
