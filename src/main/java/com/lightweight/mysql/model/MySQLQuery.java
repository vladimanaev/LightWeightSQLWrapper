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
import java.util.List;

/**
 * This class should serve as Query to MySQL database
 * 
 * @author Vladi - 01:09 AM 9/12/2013
 */
public class MySQLQuery {

	private String query;
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public enum ParameterType {
		STRING, INTEGER, DOUBLE, FLOAT
	}

	private class Parameter {
		private String value;
		private ParameterType parameterType;

		public Parameter(String value, ParameterType parameterType) {
			this.setValue(value);
			this.setParameterType(parameterType);
		}

		public ParameterType getParameterType() {
			return parameterType;
		}
		
		public void setParameterType(ParameterType parameterType) {
			this.parameterType = parameterType;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public MySQLQuery() {
		this.query = "";
	}

	public MySQLQuery(String queryString) {
		this.query = queryString;
	}
	
	public void addParameters(List<Parameter> parameters) {
		this.parameters.addAll(parameters);
	}

	public void addParameter(String value, ParameterType parameterType) {
		this.parameters.add(new Parameter(value, parameterType));
	}

	public void addStringParameter(String value) {
		this.parameters.add(new Parameter(value, ParameterType.STRING));
	}

	public void addIntParameter(int value) {
		this.parameters.add(new Parameter(Integer.toString(value), ParameterType.INTEGER));
	}

	public void addDoubleParameter(double value) {
		this.parameters.add(new Parameter(Double.toString(value), ParameterType.DOUBLE));
	}

	public void addFloatParameter(float value) {
		this.parameters.add(new Parameter(Float.toString(value), ParameterType.FLOAT));
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

	public void setQueryString(String queryString) {
		this.query = queryString;
	}

	@Override
	public String toString() {
		return this.query;
	}

	//TODO impl equals, hash
}
