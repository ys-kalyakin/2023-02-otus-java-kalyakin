package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EvenSecondsErrorProcessorTest {

    @Test
    void process() {
        var message = new Message.Builder(1).build();

        var withErrorProcessor = new EvenSecondsErrorProcessor(() -> Instant.ofEpochSecond(10));
        assertThatThrownBy(() -> withErrorProcessor.process(message)).hasMessage("even second");

        var withoutErrorProcessor = new EvenSecondsErrorProcessor(() -> Instant.ofEpochSecond(11));
        withoutErrorProcessor.process(message);
        assertDoesNotThrow(() -> withoutErrorProcessor.process(message));
    }
}