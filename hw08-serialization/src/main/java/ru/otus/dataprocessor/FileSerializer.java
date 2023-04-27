package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        if (data != null) {
            try (var fileWriter = new FileWriter(fileName)) {
                var gson = new Gson();
                var jsonWriter = gson.newJsonWriter(fileWriter);
                jsonWriter.beginObject();
                data.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            try {
                                jsonWriter.name(entry.getKey());
                                jsonWriter.value(entry.getValue());
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
                jsonWriter.endObject();

            } catch (IOException | UncheckedIOException e) {
                throw new FileProcessException(e);
            }
        }
    }
}
