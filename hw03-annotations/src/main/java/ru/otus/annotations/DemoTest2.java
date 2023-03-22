package ru.otus.annotations;

public class DemoTest2 {
    @Before
    /* private */ public void init() {
        System.out.println(this.getClass().getName() + ":before");
    }

    @Test
    public void test() {
        System.out.println(this.getClass().getName() + ":test");
    }

    @After
    public void tearDown() {
        System.out.println(this.getClass().getName() + ":after");
    }
}
