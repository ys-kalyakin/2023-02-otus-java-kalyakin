package ru.otus.api.model;


import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class SensorData implements Delayed {
    private final LocalDateTime measurementTime;
    private final String room;
    private final Double value;

    public SensorData(LocalDateTime measurementTime, String room, Double value) {
        this.measurementTime = measurementTime;
        this.room = room;
        this.value = value;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public String getRoom() {
        return room;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return -1;
    }

    @Override
    public int compareTo(Delayed o) {
        SensorData sensorData = (SensorData) o;
        return measurementTime.compareTo(sensorData.measurementTime);
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "measurementTime=" + measurementTime +
                ", room='" + room + '\'' +
                ", value=" + value +
                '}';
    }
}
