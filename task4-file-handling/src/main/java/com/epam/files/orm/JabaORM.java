package com.epam.files.orm;

import com.epam.files.db.DatabaseConfig;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JabaORM {

    public void save(Object entity) throws Exception {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);
        List<Field> fields = getAllFields(clazz);
        Field idField = getIdField(fields);
        List<Field> columnFields = getColumnFields(fields);

        try (JdbcRowSet rowSet = createRowSet()) {
            rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rowSet.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowSet.setCommand("SELECT * FROM " + tableName + " WHERE 1 = 0");
            rowSet.execute();

            rowSet.moveToInsertRow();
            for (Field f : columnFields) {
                f.setAccessible(true);
                rowSet.updateObject(f.getAnnotation(Column.class).name(), f.get(entity));
            }
            rowSet.insertRow();
            rowSet.moveToCurrentRow();
        }

        if (idField != null) {
            try (JdbcRowSet idRowSet = createRowSet()) {
                idRowSet.setCommand("SELECT MAX(" + getIdColumnName(idField) + ") FROM " + tableName);
                idRowSet.execute();
                if (idRowSet.next()) {
                    idField.setAccessible(true);
                    Object genId = idRowSet.getObject(1);
                    if (genId instanceof Number num) {
                        if (idField.getType() == Long.class || idField.getType() == long.class) {
                            idField.set(entity, num.longValue());
                        } else if (idField.getType() == Integer.class || idField.getType() == int.class) {
                            idField.set(entity, num.intValue());
                        }
                    }
                }
            }
        }
    }

    public <T> T load(Class<T> clazz, Object id) throws Exception {
        String tableName = getTableName(clazz);
        List<Field> fields = getAllFields(clazz);
        Field idField = getIdField(fields);
        if (idField == null) {
            throw new IllegalArgumentException("No @Id field found on class " + clazz.getName());
        }

        String idColName = getIdColumnName(idField);
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColName + " = ?";

        try (JdbcRowSet rowSet = createRowSet()) {
            rowSet.setCommand(sql);
            rowSet.setObject(1, id);
            rowSet.execute();

            if (rowSet.next()) {
                T instance = clazz.getDeclaredConstructor().newInstance();
                for (Field f : fields) {
                    f.setAccessible(true);
                    if (f.isAnnotationPresent(Id.class)) {
                        Object val = rowSet.getObject(idColName);
                        if (val instanceof Number num && (f.getType() == Long.class || f.getType() == long.class)) {
                            f.set(instance, num.longValue());
                        } else {
                            f.set(instance, val);
                        }
                    } else if (f.isAnnotationPresent(Column.class)) {
                        var col = f.getAnnotation(Column.class);
                        f.set(instance, rowSet.getObject(col.name()));
                    }
                }
                return instance;
            }
        }
        return null;
    }

    public void update(Object entity) throws Exception {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);
        List<Field> fields = getAllFields(clazz);
        Field idField = getIdField(fields);
        if (idField == null) {
            throw new IllegalArgumentException("No @Id field found on class " + clazz.getName());
        }

        idField.setAccessible(true);
        Object idVal = idField.get(entity);
        String idColName = getIdColumnName(idField);
        List<Field> columnFields = getColumnFields(fields);

        try (JdbcRowSet rowSet = createRowSet()) {
            rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rowSet.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowSet.setCommand("SELECT * FROM " + tableName + " WHERE " + idColName + " = ?");
            rowSet.setObject(1, idVal);
            rowSet.execute();

            if (rowSet.next()) {
                for (Field f : columnFields) {
                    f.setAccessible(true);
                    rowSet.updateObject(f.getAnnotation(Column.class).name(), f.get(entity));
                }
                rowSet.updateRow();
            }
        }
    }

    public void delete(Object entity) throws Exception {
        Class<?> clazz = entity.getClass();
        String tableName = getTableName(clazz);
        List<Field> fields = getAllFields(clazz);
        Field idField = getIdField(fields);
        if (idField == null) {
            throw new IllegalArgumentException("No @Id field found on class " + clazz.getName());
        }

        idField.setAccessible(true);
        Object idVal = idField.get(entity);
        String idColName = getIdColumnName(idField);

        try (JdbcRowSet rowSet = createRowSet()) {
            rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rowSet.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowSet.setCommand("SELECT * FROM " + tableName + " WHERE " + idColName + " = ?");
            rowSet.setObject(1, idVal);
            rowSet.execute();

            if (rowSet.next()) {
                rowSet.deleteRow();
            }
        }
    }

    private JdbcRowSet createRowSet() throws SQLException {
        JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
        rowSet.setUrl(DatabaseConfig.getUrl());
        rowSet.setUsername(DatabaseConfig.getUser());
        rowSet.setPassword(DatabaseConfig.getPassword());
        return rowSet;
    }

    private String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || table.name().isEmpty()) {
            return clazz.getSimpleName().toLowerCase();
        }
        return table.name();
    }

    private String getIdColumnName(Field idField) {
        Column col = idField.getAnnotation(Column.class);
        return col != null && !col.name().isEmpty() ? col.name() : idField.getName();
    }

    private Field getIdField(List<Field> fields) {
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }
        return null;
    }

    private List<Field> getColumnFields(List<Field> fields) {
        List<Field> list = new ArrayList<>();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Column.class) && !f.isAnnotationPresent(Id.class)) {
                list.add(f);
            }
        }
        return list;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            Collections.addAll(fields, current.getDeclaredFields());
            current = current.getSuperclass();
        }
        return fields;
    }
}
