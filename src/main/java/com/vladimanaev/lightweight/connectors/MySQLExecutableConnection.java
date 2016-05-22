/*******************************************************************************
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.vladimanaev.lightweight.connectors;

import com.vladimanaev.lightweight.exceptions.IllegalRowState;
import com.vladimanaev.lightweight.exceptions.IllegalSQLQueryException;
import com.vladimanaev.lightweight.model.*;
import com.vladimanaev.lightweight.model.Column;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Vladi
 * Date: 5/20/2016
 * Time: 1:13 AM
 * Copyright VMSR
 */
public class MySQLExecutableConnection implements ExecutableConnection {

    private final Connection connection;

    public MySQLExecutableConnection(Connection connection) {
        if(connection == null) {
            throw new IllegalStateException("Connection obj is null");
        }

        this.connection = connection;
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    /**
     * Insert, Update, Drop, Delete, Create, Alter query to MySQL database.
     */
    @Override
    public void executeUpdateQuery(Query query) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query.toString());
            updatePreparedStatementWithParameters(preparedStatement, query);
            preparedStatement.executeUpdate();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * Select query to MySQL database.
     */
    @Override
    public Result executeSelectQuery(Query query) throws SQLException {
        Result result = new Result();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query.toString());
            updatePreparedStatementWithParameters(preparedStatement, query);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultMetaData = resultSet.getMetaData();

            int maxNumberOfColumns = resultMetaData.getColumnCount();

            while (resultSet.next()) {
                Row currRow = new Row();
                for (int columnNumber = 1; columnNumber <= maxNumberOfColumns; columnNumber++) {
                    currRow.add(new Column(resultMetaData.getColumnName(columnNumber), resultSet.getString(columnNumber)));
                }

                result.add(currRow);
            }

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return result;
    }

    @Override
    public <T> List<T> executeSelectQuery(Class<T> resultClassObj, Supplier<T> resultClassObjCreator, Function<Field, String> getColumnNameFunc, Query query) throws SQLException {
        List<T> res = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query.toString());
            updatePreparedStatementWithParameters(preparedStatement, query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                T obj = resultClassObjCreator.get();
                List<Field> allFields = getAllFields(obj.getClass());

                for (Field f : allFields) {
                    if(!isStaticField(f)) {
                        f.setAccessible(true);
                        String suppliedColumnName = getColumnNameFunc.apply(f);
                        if (suppliedColumnName != null) {
                            setField(obj, f, resultSet, suppliedColumnName);
                        } else {
                            //in case of no annotation supplied taking field name
                            setField(obj, f, resultSet, f.getName());
                        }
                    }
                }

                res.add(obj);
            }

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return res;
    }

    private static boolean isStaticField(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type ; c != null ; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    private static void setField(Object target, Field f, ResultSet row, String columnName) throws SQLException {
        try {
            Class<?> type = f.getType();
            if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
                f.set(target, row.getLong(columnName));
            } else if (String.class.isAssignableFrom(type)) {
                f.set(target, row.getString(columnName));
            } else if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
                f.set(target, row.getInt(columnName));
            } else if(Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
                f.set(target, row.getDouble(columnName));
            } else if(Enum.class.isAssignableFrom(type)) {
                f.set(target, Enum.valueOf((Class<Enum>) type, row.getString(columnName)));
            } else if(Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
                f.set(target, row.getBoolean(columnName));
            }  else if(Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
                f.set(target, row.getFloat(columnName));
            }  else if(Date.class.isAssignableFrom(type)) {
                f.set(target, row.getDate(columnName));
            }
            //TODO add support for short, byte, char
        } catch (IllegalAccessException e) {
            throw new IllegalRowState(String.format("Offending column [%s]", columnName));
        }
    }

    /**
     * Handling prepared statement and avoiding SQL injections
     */
    private void updatePreparedStatementWithParameters(PreparedStatement preparedStatement, Query Query) throws SQLException {

        for (int i = 0; i < Query.getNumberOfParameters(); i++) {
            Query.PreparedStatementParameter currParameter = Query.getParameter(i);
            int currentIndex = i + 1;
            switch (currParameter.getJdbcType()) {
                case BOOLEAN:
                case TINYINT:
                    preparedStatement.setBoolean(currentIndex, Boolean.parseBoolean(currParameter.getValue()));
                    break;
                case INTEGER:
                    preparedStatement.setInt(currentIndex, Integer.parseInt(currParameter.getValue()));
                    break;
                case BIGINT:
                    preparedStatement.setLong(currentIndex, Long.parseLong(currParameter.getValue()));
                    break;
                case FLOAT:
                    preparedStatement.setFloat(currentIndex, Float.parseFloat(currParameter.getValue()));
                    break;
                case DOUBLE:
                    preparedStatement.setDouble(currentIndex, Double.parseDouble(currParameter.getValue()));
                    break;
                case CHAR:
                case VARCHAR:
                case LONGVARCHAR:
                    preparedStatement.setString(currentIndex, currParameter.getValue());
                    break;
                case DATE:
                case TIMESTAMP:
                    preparedStatement.setDate(currentIndex, Date.valueOf((currParameter.getValue())));
                    break;
                default:
                    throw new IllegalSQLQueryException("Unsupported JDBC type");
            }
        }
    }
}
