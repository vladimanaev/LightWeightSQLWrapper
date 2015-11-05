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
 * This class should serve as Result from MySQL database query
 * 
 * @author Vladi - 07:30 PM 9/12/2013
 */
public class MySQLResult extends ArrayList<MySQLRow> {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(MySQLRow curr : this) {
			builder.append(curr.toString());
			builder.append(System.getProperty("line.separator"));
		}
		
		return builder.toString();
	}
}