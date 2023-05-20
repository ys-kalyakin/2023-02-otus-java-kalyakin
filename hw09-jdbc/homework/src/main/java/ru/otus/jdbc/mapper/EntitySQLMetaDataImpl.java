package ru.otus.jdbc.mapper;

import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Генератор sql по данным мета-информации
 */
@AllArgsConstructor
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    @Override
    public String getSelectAllSql() {
        return "select " + getFieldsString(entityClassMetaData.getAllFields()) + " from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "select " + getFieldsString(entityClassMetaData.getAllFields()) + " from " + entityClassMetaData.getName() + " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        return "insert into " + entityClassMetaData.getName() + "(" + getFieldsString(entityClassMetaData.getFieldsWithoutId()) +
                ") values (" +
                IntStream.range(0, entityClassMetaData.getFieldsWithoutId().size()).mapToObj(i -> "?").collect(Collectors.joining(","))
                + ")";
    }

    @Override
    public String getUpdateSql() {
        return "update " + entityClassMetaData.getName() + " set " +
               entityClassMetaData.getAllFields().stream().map(f -> f.getName() + " = ?").collect(Collectors.joining(",")) +
               " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    private String getFieldsString(List<Field> fieldList) {
        return fieldList.stream().map(Field::getName).collect(Collectors.joining(","));
    }
}
