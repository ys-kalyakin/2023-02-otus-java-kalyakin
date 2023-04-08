package ru.otus.bytecode;

public class Main {
    public static void main(String[] args) {
        TestLogging.newInstance().calculation(1, 2);
        TestLogging.newInstance().calculation(2, 2);
        TestLogging.newInstance().calculation(1);
        TestLogging.newInstance().calculation(1, 3, "4");
    }
}
