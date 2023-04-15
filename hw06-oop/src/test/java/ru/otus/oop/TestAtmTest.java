package ru.otus.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.oop.banknote.StandardBanknote;
import ru.otus.oop.ceil.StandardCeil;
import ru.otus.oop.exceptions.CapacityIsOverException;
import ru.otus.oop.exceptions.NotEnoughMoneyException;

import static ru.otus.oop.denominations.Denomination.Demominations.*;

class TestAtmTest {

    @Test
    void addCeil() {
        TestAtm atm = new TestAtm(2);
        atm.addCeil(new StandardCeil(1, FIVE_THOUSAND));
        atm.addCeil(new StandardCeil(1, ONE_HUNDRED));
        Assertions.assertThrows(CapacityIsOverException.class, () -> atm.addCeil(new StandardCeil(1, ONE_THOUSAND)));
    }

    @Test
    void addBanknote() {
        Atm atm = new TestAtm(2);
        atm.addCeil(new StandardCeil(1, ONE_HUNDRED));
        Assertions.assertThrows(CapacityIsOverException.class, () -> atm.addBankNote(new StandardBanknote(ONE_THOUSAND)));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        Assertions.assertThrows(CapacityIsOverException.class, () -> atm.addBankNote(new StandardBanknote(ONE_HUNDRED)));
        atm.addCeil(new StandardCeil(1, ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        Assertions.assertThrows(CapacityIsOverException.class, () -> atm.addBankNote(new StandardBanknote(ONE_HUNDRED)));
    }

    @Test
    void getCash() {
        TestAtm atm = new TestAtm(2);
        atm.addCeil(new StandardCeil(100, ONE_HUNDRED));
        atm.addCeil(new StandardCeil(1, ONE_THOUSAND));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_THOUSAND));

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> atm.getCash(1600));
        Assertions.assertEquals(1, atm.getCash(1000).size());
        Assertions.assertEquals(1, atm.getCash(100).size());
        Assertions.assertEquals(2, atm.getCash(200).size());

        atm.addBankNote(new StandardBanknote(ONE_THOUSAND));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        Assertions.assertEquals(4, atm.getCash(1300).size());

        atm.addBankNote(new StandardBanknote(ONE_THOUSAND));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        atm.addBankNote(new StandardBanknote(ONE_HUNDRED));
        Assertions.assertThrows(NotEnoughMoneyException.class, () -> atm.getCash(1600));
        Assertions.assertThrows(NotEnoughMoneyException.class, () -> atm.getCash(1));
    }
}