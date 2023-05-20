package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер ResultSet в определенную сущность
 *
 * @param <T> тип сущности
 */
@FunctionalInterface
public interface RowMapper<T> {
    /**
     * выполнить маппинг ResultSet в сущность
     *
     * @param rs ResultSet
     * @return объект сущности
     */
    T mapRow(final ResultSet rs) throws SQLException;
}
