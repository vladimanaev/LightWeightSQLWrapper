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

package mysql;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class should serve as Query to MySQL database
 * 
 * @author Vladi - 01:09 AM 9/12/2013
 */
public class Query
{
	/**
	 * <p>
	 * Regex compiler pattern
	 * </p>
	 * <p>
	 * <b>By Default Allows:</b> [^a-z0-9 %|'|;|*|,|.|_|=|(|)|:|?|/|!|[|]|+|\"|{|}|$|@|\u0590-\u05FF|^\\s*$]
	 * </p>
	 */
	private static String regexCompilerPattern = "[^a-z0-9 %|'|;|*|,|.|_|=|(|)|:|?|/|!|[|]|+|\"|{|}|$|@|\u0590-\u05FF|^\\s*$]";
	private String query;
	private QueryState queryState;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	public static enum QueryState
	{
		Ilegal, Legal
	}

	public static enum ParameterType
	{
		text, intNumber, doubleNumber, floatNumber
	}

	private class Parameter
	{
		private String value;
		private ParameterType parameterType;

		public Parameter(String value, ParameterType parameterType)
		{
			this.setValue(value);
			this.setParameterType(parameterType);
		}

		public ParameterType getParameterType()
		{
			return parameterType;
		}

		public void setParameterType(ParameterType parameterType)
		{
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

	public Query()
	{
		this.queryState = QueryState.Ilegal;
		this.query = "";
	}

	public Query(String queryString, QueryState state)
	{
		this.query = queryString;
		this.queryState = state;
	}

	/**
	 * <code>public void addParameter(String value, ParameterType parameterType)</code>
	 * <p>
	 * Adds new parameter
	 * </p>
	 * <p>
	 * <b>example: </b> <em>
	 * 		select * from table where column1 = ? and column2 = ?;
	 * </em>
	 * </p>
	 * <p>
	 * Parameters are replacing those '?' while connector will create prepared statement.
	 * </p>
	 * The order is from left to right.
	 * 
	 * @param value
	 * @param parameterType
	 */
	public void addParameter(String value, ParameterType parameterType)
	{
		this.parameters.add(new Parameter(value, parameterType));
	}

	public void addTextParameter(String value)
	{
		this.parameters.add(new Parameter(value, Query.ParameterType.text));
	}

	public void addIntParameter(int value)
	{
		this.parameters.add(new Parameter(Integer.toString(value), Query.ParameterType.intNumber));
	}

	public void addDoubleParameter(double value)
	{
		this.parameters.add(new Parameter(Double.toString(value), Query.ParameterType.doubleNumber));
	}

	public void addFloatParameter(float value)
	{
		this.parameters.add(new Parameter(Float.toString(value), Query.ParameterType.floatNumber));
	}

	/**
	 * @param index
	 * @return value of parameter number 'index'
	 */
	public String getParameterValue(int index)
	{
		return this.parameters.get(index).getValue();
	}

	/**
	 * @param index
	 * @return type of parameter number 'index'
	 */
	public ParameterType getParameterType(int index)
	{
		return this.parameters.get(index).getParameterType();
	}

	/**
	 * @return number of parameters
	 */
	public int getNumberOfParameters()
	{
		return this.parameters.size();
	}

	public void setQuery(String queryString, QueryState state)
	{
		this.query = queryString;
		this.queryState = state;
	}

	public void setQueryString(String queryString)
	{
		if (queryString.isEmpty() || queryString == null)
		{
			this.query = "";
			this.queryState = QueryState.Ilegal;
		} else
		{
			this.query = queryString;
			this.queryState = QueryState.Legal;
		}
	}

	public QueryState getQueryState()
	{
		return this.queryState;
	}

	public boolean isQueryValid()
	{
		return (queryState == QueryState.Legal) ? true : false;
	}

	@Override
	public String toString()
	{
		return this.query;
	}

	/**
	 * <p>
	 * Change default regex pattern into your own pattern.
	 * </p>
	 * 
	 * @param regexPattern
	 */
	public static void setRegexPattern(String regexPattern)
	{
		Query.regexCompilerPattern = regexPattern;
	}

	/**
	 * Using Regex to check if input characters are legal in given query.
	 * <p>
	 * I strongly suggest to use your own pattern
	 * <p>
	 * (use <code>setRegexPattern(...)</code> method to do so).
	 * </p>
	 * <b>By Default Allows:</b> [^a-z0-9 %|'|;|*|,|.|_|=|(|)|:|?|/|!|[|]|+|\"|{|}|$|@|\u0590-\u05FF|^\\s*$] </p>
	 * 
	 * @param query
	 * @return true if query string legal according to given regex (see default regex on <code>setRegexPattern(..)</code> method).
	 */
	public static boolean isThisStringLigalAccordingToRegexPattern(String query)
	{
		if (query != null)
		{
			Pattern pattern = Pattern.compile(regexCompilerPattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(query);

			return (matcher.find() ? false : true);

		} else
		{
			return false;
		}
	}
}
