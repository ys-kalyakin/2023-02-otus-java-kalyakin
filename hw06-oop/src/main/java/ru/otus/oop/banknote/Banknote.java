package ru.otus.oop.banknote;

import ru.otus.oop.denominations.Denomination;

/**
 * банкнота
 */
public interface Banknote {
    /**
     * @return номинал
     */
    Denomination denomination();
}
