package ru.otus.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table("client")
public class Client {
    @Id
    private final Long id;
    private final String name;
    @MappedCollection(idColumn = "client_id")
    private final Address address;
    @MappedCollection(idColumn = "client_id", keyColumn = "number")
    private final List<Phone> phones;

    public Client(String name, Address address, List<Phone> phones) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = new Address(address.getId(), address.getStreet(), this);
        this.phones = new ArrayList<>();
        phones.forEach(p -> this.phones.add(new Phone(p.getId(), p.getNumber(), this)));
    }
}
