package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Client> dataTemplate,
            HwCache<String, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                cache.put(String.valueOf(clientId), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            cache.put(String.valueOf(client.getId()), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var result = cache.get(String.valueOf(id));

        if (result == null) {
            return transactionRunner.doInTransaction(connection -> {
                var clientOptional = dataTemplate.findById(connection, id);
                log.info("client: {}", clientOptional);
                clientOptional.ifPresent(c -> cache.put(String.valueOf(c.getId()), c));
                return clientOptional;
            });
        }
        return Optional.of(result);
    }

    @Override
    public List<Client> findAll() {
        // кэш неиспользуем, т.к. возможно он содержит не всех клиентов
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
