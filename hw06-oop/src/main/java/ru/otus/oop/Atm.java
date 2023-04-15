package ru.otus.oop;

import ru.otus.oop.banknote.Banknote;
import ru.otus.oop.ceil.Ceil;

import java.util.List;

/**
 * Интерфейс банкомата
 */
public interface Atm {
    /**
     * добавить ячейку в банкомат
     *
     * @param ceil ячейка
     */
    void addCeil(Ceil ceil);

    /**
     * добавить банкноту в банкомат
     *
     * @param banknote банкнота
     */
    void addBankNote(Banknote banknote);

    /**
     * добавить список банкнот
     *
     * @param banknotes банкноты
     */
    default void addBankNotes(List<Banknote> banknotes) {
        banknotes.forEach(this::addBankNote);
    }

    /**
     * получить наличные
     *
     * @param value количество
     * @return банкноты
     */
    List<Banknote> getCash(int value);
}
