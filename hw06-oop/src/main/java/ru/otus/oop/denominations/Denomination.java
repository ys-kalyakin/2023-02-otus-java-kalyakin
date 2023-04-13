package ru.otus.oop.denominations;

/**
 * номинал банкноты
 */
public interface Denomination {
    /**
     * @return размер номинала
     */
    int getValue();

    enum Demominations implements Denomination {
        FIVE_THOUSAND(5000),
        ONE_THOUSAND(1000),
        FIVE_HUNDRED(500),
        ONE_HUNDRED(100),
        FIFTY(50),
        ;

        private final int value;

        Demominations(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }
}