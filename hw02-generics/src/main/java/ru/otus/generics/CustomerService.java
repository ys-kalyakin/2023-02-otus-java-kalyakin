package ru.otus.generics;


import java.util.*;
import java.util.function.Function;

public class CustomerService {
    private static final Function<Map.Entry<Customer, String>, Map.Entry<Customer, String>> MAPPER =
            e -> Map.entry(new Customer(e.getKey()), e.getValue());

    private final NavigableMap<Customer, String> customersMap;

    public CustomerService() {
        this.customersMap = new TreeMap<>(Comparator.comparing(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        return Optional.ofNullable(customersMap.firstEntry())
                .map(MAPPER)
                .orElse(null);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return Optional.ofNullable(customersMap.higherEntry(customer))
                .map(MAPPER)
                .orElse(null);
    }

    public void add(Customer customer, String data) {
        customersMap.put(new Customer(customer), data);
    }
}
