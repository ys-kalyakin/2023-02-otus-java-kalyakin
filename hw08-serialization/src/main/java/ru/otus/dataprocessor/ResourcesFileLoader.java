package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var reader = new InputStreamReader(requireNonNull(this.getClass().getClassLoader().getResourceAsStream(fileName)))) {
            var gson = new Gson();
            return Arrays.stream(gson.fromJson(reader, Measurement[].class)).toList();
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
