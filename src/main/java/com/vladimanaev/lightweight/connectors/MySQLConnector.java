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

import com.vladimanaev.lightweight.exceptions.IllegalSQLQueryException;
import com.vladimanaev.lightweight.model.Column;
import com.vladimanaev.lightweight.model.Query;
import com.vladimanaev.lightweight.model.Result;
import com.vladimanaev.lightweight.model.Row;
import com.vladimanaev.lightweight.mysql.DriverManagerWrapper;

import java.sql.*;

/**
 * This class should serve as adapter to MySQL database
 * @version 2.0
 * @author Vladi - 8:09 PM 9/12/2013
*/
public class MySQLConnector implements SQLConnector {
	private Connection connection = null;

	private String url;
	private String user;
	private String password;
    private DriverManagerWrapper driverManagerWrapper;

	public MySQLConnector(DriverManagerWrapper driverManagerWrapper, String url, String user, String password) {
        this.driverManagerWrapper = driverManagerWrapper;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Opens connection to MySQL database
	 */
    @Override
	public void open() throws ClassNotFoundException, SQLException {
		connection = driverManagerWrapper.getConnection(url, user, password);
	}

	/**
	 * Closing connection to MySQL database
	 */
    @Override
    public void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * Checks if connection to Database is open
	 */
    @Override
    public boolean isConnected() throws SQLException {
		boolean isConnected = false;

		if (connection != null) {
			isConnected = !connection.isClosed();
		}

		return isConnected;
	}

    @Override
    public boolean commit() throws SQLException {
		if(isConnected()) {
			connection.commit();
			return true;
		}

		return false;
	}

    @Override
    public boolean rollback() throws SQLException {
		if(isConnected()) {
			connection.rollback();
			return true;
		}
		
		return false;
	}

    @Override
    public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Insert, Update, Drop, Delete, Create, Alter query to MySQL database.
	 */
    @Override
    public void executeUpdateQuery(Query query) throws SQLException, IllegalSQLQueryException, ClassNotFoundException {
		PreparedStatement preparedStatement = null;
		try {
			if (!isConnected()) {
				open();
			}

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
    public Result executeSelectQuery(Query query) throws IllegalSQLQueryException, ClassNotFoundException, SQLException {
		Result result = new Result();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if (!isConnected()) {
				open();
			}
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

	/**
	 * Handling prepared statement and avoiding SQL injections
	 */
	private void updatePreparedStatementWithParameters(PreparedStatement preparedStatement, Query Query) throws SQLException, IllegalSQLQueryException {

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
