package ru.otus.jdbc.mapper;

import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Метаданные класса сущности
 *
 * @param <T> тип сущности
 */
@AllArgsConstructor
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> entityClass;


    @Override
    public String getName() {
        return entityClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return entityClass.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    @Override
    public Field getIdField() {
        return getAllFields().stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(PropertyUtils.getPropertyDescriptors(entityClass))
                .map(p -> {
                    try {
                        return entityClass.getDeclaredField(p.getName());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream().filter(f -> !f.equals(getIdField())).toList();
    }
}
