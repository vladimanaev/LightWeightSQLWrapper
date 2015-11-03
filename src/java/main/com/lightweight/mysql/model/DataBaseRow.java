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
 * This class serve as row in MySQL database
 * 
 * @author Vladi - 01:59 PM 9/12/2013
 */
public class DataBaseRow {
	private ArrayList<DataBaseColumn> row = new ArrayList<DataBaseColumn>();

	public DataBaseColumn getColumn(int index) {
		return this.row.get(index);
	}

	public void addColumn(DataBaseColumn dataBaseColumn) {
		this.row.add(dataBaseColumn);
	}

	public int getNumOfColumns() {
		return this.row.size();
	}

	public void clear() {
		this.row.clear();
	}
}