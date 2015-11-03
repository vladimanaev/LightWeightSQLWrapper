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

package com.lightweight.mysql.model;

import java.util.ArrayList;

/**
 * This class should serve as Query to MySQL database
 * 
 * @author Vladi - 01:09 AM 9/12/2013
 */
public class MySQLQuery {

	private String query;
	private QueryState queryState;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	public enum QueryState {
		Ilegal, Legal
	}

	public enum ParameterType {
		text, intNumber, doubleNumber, floatNumber
	}

	private class Parameter {
		private String value;
		private ParameterType parameterType;

		public Parameter(String value, ParameterType parameterType)
		{
			this.setValue(value);
			this.setParameterType(parameterType);
		}

		public ParameterType getParameterType() {
			return parameterType;
		}
		
		public void setParameterType(ParameterType parameterType) {
			this.parameterType = parameterType;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}

	public MySQLQuery() {
		this.queryState = QueryState.Ilegal;
		this.query = "";
	}

	public MySQLQuery(String queryString, QueryState state) {
		this.query = queryString;
		this.queryState = state;
	}

	public void addParameter(String value, ParameterType parameterType) {
		this.parameters.add(new Parameter(value, parameterType));
	}

	public void addTextParameter(String value) {
		this.parameters.add(new Parameter(value, MySQLQuery.ParameterType.text));
	}

	public void addIntParameter(int value) {
		this.parameters.add(new Parameter(Integer.toString(value), MySQLQuery.ParameterType.intNumber));
	}

	public void addDoubleParameter(double value) {
		this.parameters.add(new Parameter(Double.toString(value), MySQLQuery.ParameterType.doubleNumber));
	}

	public void addFloatParameter(float value) {
		this.parameters.add(new Parameter(Float.toString(value), MySQLQuery.ParameterType.floatNumber));
	}

	public String getParameterValue(int index) {
		return this.parameters.get(index).getValue();
	}

	public ParameterType getParameterType(int index) {
		return this.parameters.get(index).getParameterType();
	}

	public int getNumberOfParameters() {
		return this.parameters.size();
	}

	public void setQuery(String queryString, QueryState state) {
		this.query = queryString;
		this.queryState = state;
	}

	public void setQueryString(String queryString) {
		if (queryString == null || queryString.isEmpty()) {
			this.query = "";
			this.queryState = QueryState.Ilegal;
		} else {
			this.query = queryString;
			this.queryState = QueryState.Legal;
		}
	}

	public QueryState getQueryState() {
		return this.queryState;
	}

	public boolean isQueryValid() {
		return (queryState == QueryState.Legal);
	}

	@Override
	public String toString() {
		return this.query;
	}
	
}
