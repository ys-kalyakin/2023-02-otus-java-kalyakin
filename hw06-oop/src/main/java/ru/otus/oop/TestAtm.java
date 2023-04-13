package ru.otus.oop;

import ru.otus.oop.banknote.Banknote;
import ru.otus.oop.ceil.Ceil;
import ru.otus.oop.denominations.Denomination;
import ru.otus.oop.exceptions.CapacityIsOverException;
import ru.otus.oop.exceptions.NotEnoughMoneyException;

import java.util.*;

/**
 * Реализация банкомата
 */
public class TestAtm implements Atm {
    private final int capacity;
    private final NavigableMap<Denomination, List<Ceil>> cells;

    /**
     * ctor
     *
     * @param capacity максимальное количество ячеек в банкомате
     */
    public TestAtm(int capacity) {
        this.capacity = capacity;
        this.cells = new TreeMap<>(Comparator.comparing(Denomination::getValue).reversed());
    }

    @Override
    public void addCeil(Ceil ceil) {
        if (cells.size() < capacity) {
            cells.computeIfAbsent(ceil.getDenomination(), k -> new ArrayList<>()).add(ceil);
        } else {
            throw new CapacityIsOverException(String.valueOf(capacity));
        }
    }

    @Override
    public void addBankNote(Banknote banknote) {
        this.cells.getOrDefault(banknote.denomination(), Collections.emptyList()).stream()
                .filter(c -> c.remainCapacity() > 0)
                .findFirst()
                .ifPresentOrElse(c -> c.addBanknote(banknote), () -> {
                    throw new CapacityIsOverException("Нет ячейки для банкнот с номиналом " + banknote.denomination().getValue());
                });
    }

    @Override
    public List<Banknote> getCash(int value) {
        if (value <= 0) {
            return Collections.emptyList();
        }

        List<Banknote> result = new ArrayList<>();
        List<Runnable> deleteActions = new ArrayList<>();
        int sum = 0;
        for (var ceil : cells.values().stream().flatMap(Collection::stream).toList()) {
            for (var banknote : ceil.getBankNotes()) {
                if (banknote.denomination().getValue() <= value && sum < value) {
                    sum += banknote.denomination().getValue();
                    result.add(banknote);
                    deleteActions.add(() -> ceil.deleteBankNote(banknote));
                }
            }
        }
        if (sum != value) {
            throw new NotEnoughMoneyException("Недостаточно средств");
        }
        // удаляем банкноты из ячеек если можем выдать сумму
        deleteActions.forEach(Runnable::run);
        return result;
    }
}
