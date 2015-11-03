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

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lightweight.mysql.exceptions.IllegalSQLQueryException;
import com.lightweight.mysql.model.DataBaseColumn;
import com.lightweight.mysql.model.DataBaseRow;
import com.lightweight.mysql.model.MySQLQuery;
import com.lightweight.mysql.model.Result;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

/**
 * This class should serve as adapter to MySQL database
 * @version 2.0
 * @author Vladi - 8:09 PM 9/12/2013
*/
public class MySQLConnector {
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet result = null;

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
	public void open() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = (Connection) DriverManager.getConnection(url, user, password);
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
		if (mySQLQuery.isQueryValid()) {
			try {
				if (!isConnected()) {
					open();
				}

				preparedStatement = (PreparedStatement) connection.prepareStatement(mySQLQuery.toString());
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
		} else
		{
			throw new IllegalSQLQueryException("Ilegal query!");
		}
	}

	/**
	 * Select query to MySQL database.
	 */
	public Result executeSelectQuery(MySQLQuery mySQLQuery) throws SQLException, IllegalSQLQueryException, ClassNotFoundException {
		Result mySQLResult = new Result();

		if (mySQLQuery.isQueryValid()) {
			try {
				if (!isConnected()) {
					open();
				}

				preparedStatement = (PreparedStatement) connection.prepareStatement(mySQLQuery.toString());
				updatePreparedStatementWithParameters(preparedStatement, mySQLQuery);
				result = preparedStatement.executeQuery();
				ResultSetMetaData resultMetaData = (ResultSetMetaData) result.getMetaData();

				int maxNumberOfColums = resultMetaData.getColumnCount();

				while (result.next()) {
					DataBaseRow row = new DataBaseRow();
					for (int columnNumber = 1; columnNumber <= maxNumberOfColums; columnNumber++) {
						DataBaseColumn column = new DataBaseColumn();

						column.setColumnName(resultMetaData.getColumnName(columnNumber));
						column.setColumnValue(result.getString(columnNumber));

						row.addColumn(column);
					}

					mySQLResult.addRow(row);
				}

			} finally {
				if (result != null) {
					result.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		} else {
			throw new IllegalSQLQueryException("Ilegal query!");
		}

		return mySQLResult;
	}

	/**
	 * Creating prepared statement in order to avoid SQL injections
	 */
	private void updatePreparedStatementWithParameters(PreparedStatement preparedStatement, MySQLQuery mySQLQuery) throws SQLException, IllegalSQLQueryException {
		// Databases counts from 1 and not 0 thats why there is (i + 1).
		for (int i = 0; i < mySQLQuery.getNumberOfParameters(); i++) {
			if (mySQLQuery.getParameterType(i) == MySQLQuery.ParameterType.text) {
				preparedStatement.setString(i + 1, mySQLQuery.getParameterValue(i));

			} else if (mySQLQuery.getParameterType(i) == MySQLQuery.ParameterType.intNumber) {
				preparedStatement.setInt(i + 1, Integer.parseInt(mySQLQuery.getParameterValue(i)));

			} else if (mySQLQuery.getParameterType(i) == MySQLQuery.ParameterType.doubleNumber) {
				preparedStatement.setDouble(i + 1, Double.parseDouble(mySQLQuery.getParameterValue(i)));

			} else if (mySQLQuery.getParameterType(i) == MySQLQuery.ParameterType.floatNumber) {
				preparedStatement.setFloat(i + 1, Float.parseFloat(mySQLQuery.getParameterValue(i)));

			} else {
				throw new IllegalSQLQueryException("Invalid type of Query.ParameterType!");
			}
		}
	}
}
