package ru.otus.annotations.tests;

import ru.otus.annotations.Asserts;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class DemoTest1 {
    private int count;

    @Before
    public void init() {
        count = 10;
    }

    @Test
    public void testDec() {
        Asserts.equals(--count, 9);
    }

    @Test
    public  void testDecFail() {
        Asserts.equals(count--, 9);
    }
}
