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

package com.vladimanaev.lightweight.model;

import java.sql.JDBCType;
import java.util.LinkedList;
import java.util.List;

/**
 * This class should serve as Query to MySQL database
 * 
 * @author Vladi - 01:09 AM 9/12/2013
 */
public class Query {

	private final String query;
	private final List<PreparedStatementParameter> parameters = new LinkedList<PreparedStatementParameter>();
	
	public static class PreparedStatementParameter {
		private final String value;
		private final JDBCType jdbcType;

		public PreparedStatementParameter(String value, JDBCType jdbcType) {
			this.value = value;
			this.jdbcType = jdbcType;
		}

		public JDBCType getJdbcType() {
			return jdbcType;
		}

		public String getValue() {
			return value;
		}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PreparedStatementParameter that = (PreparedStatementParameter) o;

            if (jdbcType != that.jdbcType) return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = value != null ? value.hashCode() : 0;
            result = 31 * result + (jdbcType != null ? jdbcType.hashCode() : 0);
            return result;
        }
    }

	public Query(String queryString) {
		this.query = queryString;
	}
	
	public void addParameters(List<PreparedStatementParameter> parameters) {
		this.parameters.addAll(parameters);
	}

	public void addParameter(String value, JDBCType jdbcType) {
		parameters.add(new PreparedStatementParameter(value, jdbcType));
	}

	public PreparedStatementParameter getParameter(int index) {
		return parameters.get(index);
	}

	public int getNumberOfParameters() {
		return parameters.size();
	}

	@Override
	public String toString() {
		return query;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query that = (Query) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
