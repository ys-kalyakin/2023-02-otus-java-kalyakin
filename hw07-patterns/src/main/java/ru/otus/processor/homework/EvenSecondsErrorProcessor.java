package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.Instant;
import java.util.function.Supplier;

public class EvenSecondsErrorProcessor implements Processor {
    private final Supplier<Instant> timeSupplier;

    public EvenSecondsErrorProcessor(Supplier<Instant> timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    @Override
    public Message process(Message message) {

        if (timeSupplier.get().getEpochSecond() % 2 == 0)
            throw new RuntimeException("even second");
        return message;
    }
}
