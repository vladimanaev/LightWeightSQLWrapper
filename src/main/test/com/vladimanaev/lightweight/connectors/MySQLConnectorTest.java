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

import com.vladimanaev.lightweight.model.Query;
import com.vladimanaev.lightweight.model.Result;
import com.vladimanaev.lightweight.mysql.DriverManagerWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vladi
 * Date: 3/12/2016
 * Time: 8:34 PM
 * Copyright VMSR
 */
public class MySQLConnectorTest {

    @Test
    public void testSimpleSelect() throws Exception {
        final int numOfRows = 2;
        final int numOfColumns = 2;
        String firstColumnName = "testColumnName-1";
        String firstColumnValue = "testColumnName-1";

        String secondColumnName = "testColumnName-2";
        String secondColumnValue = "testColumnName-2";

        Query query = new Query("select * from vladi;");

        final DriverManagerWrapper driverManagerWrapperMock = mock(DriverManagerWrapper.class);
        final Connection connectionMock = mock(Connection.class);
        final PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        final ResultSet resultSetMock = mock(ResultSet.class);
        final ResultSetMetaData resultSetMetaDataMock = mock(ResultSetMetaData.class);

        when(driverManagerWrapperMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(query.toString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetaDataMock);

        when(resultSetMetaDataMock.getColumnCount()).thenReturn(numOfColumns);
        when(resultSetMetaDataMock.getColumnName(1)).thenReturn(firstColumnName);
        when(resultSetMock.getString(1)).thenReturn(firstColumnValue);

        when(resultSetMetaDataMock.getColumnCount()).thenReturn(numOfColumns);
        when(resultSetMetaDataMock.getColumnName(2)).thenReturn(secondColumnName);
        when(resultSetMock.getString(2)).thenReturn(secondColumnValue);

        when(resultSetMock.next()).then(new Answer<Boolean>() {
            private int count = 0;

            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                return count++ < numOfRows;
            }
        });

        MySQLConnector mySQLConnector = new MySQLConnector(driverManagerWrapperMock);
        Result rows = mySQLConnector.executeSelectQuery(query);

        verify(resultSetMock).close();
        verify(preparedStatementMock).close();

        Assert.assertEquals("Invalid result size", numOfRows, rows.size());

        Assert.assertEquals("Invalid column name", firstColumnName, rows.get(0).get(0).getColumnName());
        Assert.assertEquals("Invalid column value", firstColumnName, rows.get(0).get(0).getColumnValue());

        Assert.assertEquals("Invalid column name", secondColumnName, rows.get(0).get(1).getColumnName());
        Assert.assertEquals("Invalid column value", secondColumnValue, rows.get(0).get(1).getColumnValue());

        Assert.assertEquals("Invalid column name", firstColumnName, rows.get(1).get(0).getColumnName());
        Assert.assertEquals("Invalid column value", firstColumnName, rows.get(1).get(0).getColumnValue());

        Assert.assertEquals("Invalid column name", secondColumnName, rows.get(1).get(1).getColumnName());
        Assert.assertEquals("Invalid column value", secondColumnValue, rows.get(1).get(1).getColumnValue());
    }

    @Test
    public void testSimpleUpdate() throws Exception {
        Query query = new Query("update vladi set testColumnName-1 = ?;");
        query.addParameter("true", JDBCType.BOOLEAN);

        final DriverManagerWrapper driverManagerWrapperMock = mock(DriverManagerWrapper.class);
        final Connection connectionMock = mock(Connection.class);
        final PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

        when(driverManagerWrapperMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(query.toString())).thenReturn(preparedStatementMock);

        MySQLConnector mySQLConnector = new MySQLConnector(driverManagerWrapperMock);
        mySQLConnector.executeUpdateQuery(query);

        verify(preparedStatementMock).close();
        verify(preparedStatementMock).setBoolean(1, true);
    }

    @Test
    public void testPreparedStatementParameters() throws Exception {
        Query query = new Query("query does not matter here...");
        query.addParameter("true", JDBCType.BOOLEAN);
        query.addParameter("true", JDBCType.TINYINT);
        query.addParameter("1", JDBCType.INTEGER);
        query.addParameter("2", JDBCType.BIGINT);
        query.addParameter("3.0", JDBCType.FLOAT);
        query.addParameter("4.0", JDBCType.DOUBLE);
        query.addParameter("CHAR", JDBCType.CHAR);
        query.addParameter("VARCHAR", JDBCType.VARCHAR);
        query.addParameter("LONGVARCHAR", JDBCType.LONGVARCHAR);
        query.addParameter("2016-01-20", JDBCType.DATE);
        query.addParameter("2016-01-21", JDBCType.TIMESTAMP);

        final DriverManagerWrapper driverManagerWrapperMock = mock(DriverManagerWrapper.class);
        final Connection connectionMock = mock(Connection.class);
        final PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

        when(driverManagerWrapperMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(query.toString())).thenReturn(preparedStatementMock);

        MySQLConnector mySQLConnector = new MySQLConnector(driverManagerWrapperMock);
        mySQLConnector.executeUpdateQuery(query);

        verify(preparedStatementMock).close();
        verify(preparedStatementMock).setBoolean(1, true);
        verify(preparedStatementMock).setBoolean(2, true);
        verify(preparedStatementMock).setInt(3, 1);
        verify(preparedStatementMock).setLong(4, 2);
        verify(preparedStatementMock).setFloat(5, 3.0f);
        verify(preparedStatementMock).setDouble(6, 4.0f);
        verify(preparedStatementMock).setString(7, "CHAR");
        verify(preparedStatementMock).setString(8, "VARCHAR");
        verify(preparedStatementMock).setString(9, "LONGVARCHAR");
        verify(preparedStatementMock).setDate(10, Date.valueOf("2016-01-20"));
        verify(preparedStatementMock).setDate(11, Date.valueOf("2016-01-21"));
    }
}
