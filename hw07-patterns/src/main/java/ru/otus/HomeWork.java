package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondsErrorProcessor;
import ru.otus.processor.homework.SwapFieldsProcessor;

import java.time.Instant;
import java.util.List;

public class HomeWork {
    public static void main(String[] args) {
        var processors = List.of(
                new SwapFieldsProcessor(),
                new EvenSecondsErrorProcessor(Instant::now)
        );

        var complexProcessor = new ComplexProcessor(processors, System.err::println);
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
