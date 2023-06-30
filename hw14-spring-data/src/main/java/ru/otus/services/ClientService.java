package ru.otus.services;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.entity.Client;

/**
 * Сервис для работы с клиентами
 */
public interface ClientService extends ListCrudRepository<Client, Long> {
}
