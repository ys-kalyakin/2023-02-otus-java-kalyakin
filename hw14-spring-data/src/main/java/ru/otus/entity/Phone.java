package ru.otus.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@AllArgsConstructor
@Getter
public class Phone {
    @Id
    private final Long id;
    private final String number;
    @Transient
    private final Client client;

    public Phone(String number) {
        this(null, number, null);
    }

    @PersistenceCreator
    public Phone(Long id, String number) {
        this(id, number, null);
    }
}
