package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.assertj.core.api.Assertions.assertThat;

class SwapFieldsProcessorTest {

    @Test
    void process() {
        var message = new Message.Builder(1)
                .field11("field11")
                .field12("field12")
                .build();

        var processor = new SwapFieldsProcessor();
        message = processor.process(message);

        assertThat(message.getField11()).isEqualTo("field12");
        assertThat(message.getField12()).isEqualTo("field11");
    }
}