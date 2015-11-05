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

package com.lightweight.mysql;

import com.lightweight.mysql.exceptions.IllegalSQLQueryException;
import com.lightweight.mysql.model.MySQLColumn;
import com.lightweight.mysql.model.MySQLRow;
import com.lightweight.mysql.model.MySQLQuery;
import com.lightweight.mysql.model.MySQLResult;

import java.sql.*;

/**
 * This class should serve as adapter to MySQL database
 * @version 2.0
 * @author Vladi - 8:09 PM 9/12/2013
*/
public class MySQLConnector {
	private Connection connection = null;

	private String url;
	private String user;
	private String password;

	public MySQLConnector(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Opens connection to MySQL database
	 */
	public void open() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(url, user, password);
	}

	/**
	 * Closing connection to MySQL database
	 */
	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * Checks if connection to Database is open
	 */
	public boolean isConnected() throws SQLException {
		boolean isConnected = false;

		if (connection != null) {
			isConnected = !connection.isClosed();
		}

		return isConnected;
	}

	/**
	 * Insert, Update, Drop, Delete, Create, Alter query to MySQL database.
	 */
	public void executeUpdateQuery(MySQLQuery mySQLQuery) throws SQLException, IllegalSQLQueryException, ClassNotFoundException {
		PreparedStatement preparedStatement = null;
		try {
			if (!isConnected()) {
				open();
			}

			preparedStatement = connection.prepareStatement(mySQLQuery.toString());
			updatePreparedStatementWithParameters(preparedStatement, mySQLQuery);
			preparedStatement.executeUpdate();

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null)
			{
				connection.close();
			}
		}
	}

	/**
	 * Select query to MySQL database.
	 */
	public MySQLResult executeSelectQuery(MySQLQuery mySQLQuery) throws IllegalSQLQueryException, ClassNotFoundException, SQLException {
		MySQLResult mySqlResult = new MySQLResult();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if (!isConnected()) {
				open();
			}
			preparedStatement = connection.prepareStatement(mySQLQuery.toString());
			updatePreparedStatementWithParameters(preparedStatement, mySQLQuery);
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultMetaData = resultSet.getMetaData();

			int maxNumberOfColumns = resultMetaData.getColumnCount();

			while (resultSet.next()) {
				MySQLRow currRow = new MySQLRow();
				for (int columnNumber = 1; columnNumber <= maxNumberOfColumns; columnNumber++) {
					currRow.add(new MySQLColumn(resultMetaData.getColumnName(columnNumber), resultSet.getString(columnNumber)));
				}

				mySqlResult.add(currRow);
			}

		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return mySqlResult;
	}

	/**
	 * Handling prepared statement and avoiding SQL injections
	 */
	private void updatePreparedStatementWithParameters(PreparedStatement preparedStatement, MySQLQuery mySQLQuery) throws SQLException, IllegalSQLQueryException {

			for (int i = 0; i < mySQLQuery.getNumberOfParameters(); i++) {
				MySQLQuery.PreparedStatementParameter currParameter = mySQLQuery.getParameter(i);
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
						preparedStatement.setDate(currentIndex, new Date(Long.parseLong(currParameter.getValue())));
						break;
					default:
						throw new IllegalSQLQueryException("Unsupported JDBC type");
				}
			}
	}
}
