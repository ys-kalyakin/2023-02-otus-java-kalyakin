package ru.otus.oop.banknote;

import ru.otus.oop.denominations.Denomination;

/**
 * Стандартная банкнота
 *
 * @param denomination номинал
 */
public record StandardBanknote(Denomination denomination) implements Banknote {
}
