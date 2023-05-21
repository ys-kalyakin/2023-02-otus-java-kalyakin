package ru.otus.jdbc.mapper;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@AllArgsConstructor
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final RowMapper<T> rowMapper;

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                rs -> {
                    try {
                        if (rs.next())
                            return rowMapper.mapRow(rs);
                    } catch (SQLException e) {
                        log.error("Ошибка маппинга сущности", e);
                    }
                    return null;
                }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(),
                rs -> {
                    try {
                        List<T> result = new ArrayList<>();
                        while (rs.next()) {
                            result.add(rowMapper.mapRow(rs));
                        }
                        return result;
                    } catch (SQLException e) {
                        log.error("Ошибка маппинга сущности", e);
                        return null;
                    }
                }
        ).orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T client) {
        var parameters = new ArrayList<>();
        for (var field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                parameters.add(field.get(client));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                parameters
        );
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            var parameters = new ArrayList<>();
            for (var field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                parameters.add(field.get(client));
            }
            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            parameters.add(idField.get(client));
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    parameters
            );
        } catch (Exception e) {
            log.error("Ошибка при работе с БД", e);
        }
    }
}
