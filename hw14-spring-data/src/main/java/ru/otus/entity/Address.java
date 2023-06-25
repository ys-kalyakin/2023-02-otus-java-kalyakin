package ru.otus.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@AllArgsConstructor
@Getter
public class Address {
    @Id
    private final Long id;
    private final String street;
    @Transient
    private final Client client;

    public Address(String street) {
        this(null, street, null);
    }

    @PersistenceCreator
    public Address(Long id, String street) {
        this(id, street, null);
    }

    private Address withId(Long id) {
        return new Address(id, this.street, this.client);
    }
}
