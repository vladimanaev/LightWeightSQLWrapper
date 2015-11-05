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
 * This class should serve as Result from MySQL database query
 * 
 * @author Vladi - 07:30 PM 9/12/2013
 */
public class Result {
	
	private List<DataBaseRow> rows = new ArrayList<DataBaseRow>();

	public DataBaseRow getRow(int index) {
		return this.rows.get(index);
	}

	public void addRow(DataBaseRow dataBaseRow) {
		this.rows.add(dataBaseRow);
	}

	public int size() {
		return this.rows.size();
	}

	public void clear() {
		this.rows.clear();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(DataBaseRow curr : rows) {
			builder.append(curr.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	//TODO impl equals, hash
}