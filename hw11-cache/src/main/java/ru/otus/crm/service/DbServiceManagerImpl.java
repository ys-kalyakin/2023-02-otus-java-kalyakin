package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;
import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> cache;

    public DbServiceManagerImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> managerDataTemplate,
            HwCache<String, Manager> cache) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache = cache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                cache.put(String.valueOf(managerNo), createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            cache.put(String.valueOf(manager.getNo()), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        var result = cache.get(String.valueOf(no));
        if (result != null) {
            return Optional.of(result);
        }

        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            managerOptional.ifPresent(m -> cache.put(String.valueOf(m.getNo()), m));
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
       });
    }
}
