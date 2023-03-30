package ru.otus.annotations.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class DemoTestBeforeFail {
    @Before
    public void init() {
        System.out.println(this.getClass().getName() + ":before");
        throw new RuntimeException();
    }

    @Test
    public void test() {
        System.out.println(this.getClass().getName() + ":test");
    }

    @After
    public void after() {
        System.out.println(this.getClass().getName() + ":after");
    }
}
