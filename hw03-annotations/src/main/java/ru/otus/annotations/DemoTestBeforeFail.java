package ru.otus.annotations;

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
