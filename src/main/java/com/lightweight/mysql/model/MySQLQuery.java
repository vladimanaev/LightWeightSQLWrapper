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

import java.sql.Ref;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class should serve as Query to MySQL database
 * 
 * @author Vladi - 01:09 AM 9/12/2013
 */
public class MySQLQuery {

	private final String query;
	private final List<Parameter> parameters = new LinkedList<Parameter>();
	
	public enum ParameterType {
		STRING, INTEGER, DOUBLE, FLOAT
	}

	public static class Parameter {
		private final String value;
		private final ParameterType parameterType;

		public Parameter(String value, ParameterType parameterType) {
			this.value = value;
			this.parameterType = parameterType;
		}

		public ParameterType getParameterType() {
			return parameterType;
		}

		public String getValue() {
			return value;
		}
	}

	public MySQLQuery(String queryString) {
		this.query = queryString;
	}
	
	public void addParameters(List<Parameter> parameters) {
		this.parameters.addAll(parameters);
	}

	public void addParameter(String value, ParameterType parameterType) {
		parameters.add(new Parameter(value, parameterType));
	}

	public void addStringParameter(String value) {
		addParameter(value, ParameterType.STRING);
	}

	public void addIntParameter(int value) {
		addParameter(Integer.toString(value), ParameterType.INTEGER);
	}

	public void addDoubleParameter(double value) {
		addParameter(Double.toString(value), ParameterType.DOUBLE);
	}

	public void addFloatParameter(float value) {
		addParameter(Float.toString(value), ParameterType.FLOAT);
	}

	public Parameter getParameter(int index) {
		return parameters.get(index);
	}

	public int getNumberOfParameters() {
		return parameters.size();
	}

	@Override
	public String toString() {
		return query;
	}

	//TODO impl equals, hash
}
