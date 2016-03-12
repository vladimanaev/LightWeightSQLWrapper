package com.vladimanaev.lightweight.connectors;

import com.vladimanaev.lightweight.exceptions.IllegalSQLQueryException;
import com.vladimanaev.lightweight.model.Query;
import com.vladimanaev.lightweight.model.Result;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vladi
 * Date: 3/12/2016
 * Time: 8:11 PM
 * Copyright VMSR
 */
public interface SQLConnector {

    void open() throws ClassNotFoundException, SQLException;

    void close() throws SQLException;

    boolean isConnected() throws SQLException;

    boolean commit() throws SQLException;

    boolean rollback() throws SQLException;

    Connection getConnection();

    void executeUpdateQuery(Query Query) throws IllegalSQLQueryException, ClassNotFoundException, SQLException;

    Result executeSelectQuery(Query Query) throws IllegalSQLQueryException, ClassNotFoundException, SQLException;

}
