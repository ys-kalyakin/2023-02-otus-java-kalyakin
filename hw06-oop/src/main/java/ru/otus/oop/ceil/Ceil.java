package ru.otus.oop.ceil;

import ru.otus.oop.banknote.Banknote;
import ru.otus.oop.denominations.Denomination;

import java.util.List;

/**
 * ячейка банкомата
 */
public interface Ceil {
    /**
     * @return номинал ячейки
     */
    Denomination getDenomination();

    /**
     * @return оставшаяся вместимость
     */
    int remainCapacity();

    /**
     * добавить банкноту в ячейку
     *
     * @param banknote банкнота
     */
    void addBanknote(Banknote banknote);

    /**
     * изъять банкноту из ячейки
     *
     * @param banknote банкнота
     */
    void deleteBankNote(Banknote banknote);

    /**
     * получить банкноты из ячейки
     *
     * @return банкноты из ячейки
     */
    List<Banknote> getBankNotes();
}
