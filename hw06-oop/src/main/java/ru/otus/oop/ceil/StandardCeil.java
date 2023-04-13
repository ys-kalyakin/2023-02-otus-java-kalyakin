package ru.otus.oop.ceil;

import ru.otus.oop.banknote.Banknote;
import ru.otus.oop.denominations.Denomination;
import ru.otus.oop.exceptions.CapacityIsOverException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * стандартная ячейка банкомата
 */
public class StandardCeil implements Ceil {
    private final int capacity;
    private final Denomination denomination;
    private final List<Banknote> banknotes;

    /**
     * ctor
     *
     * @param capacity максимальное количество банкнот в ячейке
     * @param denomination номинал купюр в ячейке
     */
    public StandardCeil(int capacity, Denomination denomination) {
        this.capacity = capacity;
        this.denomination = denomination;
        this.banknotes = new ArrayList<>(capacity);
    }

    @Override
    public Denomination getDenomination() {
        return denomination;
    }

    @Override
    public int remainCapacity() {
        return capacity - banknotes.size();
    }

    @Override
    public void addBanknote(Banknote banknote) {
        if (!banknote.denomination().equals(denomination))
            throw new IllegalArgumentException("Неверный номинал купюры");

        if (banknotes.size() < capacity) {
            this.banknotes.add(banknote);
        } else {
            throw new CapacityIsOverException(String.valueOf(capacity));
        }
    }

    @Override
    public void deleteBankNote(Banknote banknote) {
        Iterator<Banknote> iterator = banknotes.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(banknote)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public List<Banknote> getBankNotes() {
        return Collections.unmodifiableList(banknotes);
    }
}
