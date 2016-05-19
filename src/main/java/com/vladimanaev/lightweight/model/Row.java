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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class serve as row in MySQL database
 * 
 * @author Vladi - 01:59 PM 9/12/2013
 */
public class Row extends ArrayList<Column> {

    private final HashMap<String, Column> map = new HashMap<>();

    public Row() {
        super();
    }

    public Row(int initialCapacity) {
        super(initialCapacity);
    }

    public Row(Collection<? extends Column> c) {
        super(c);
    }

    @Override
    public boolean add(Column column) {
        map.put(column.getColumnName(), column);
        return super.add(column);
    }

    @Override
    public void add(int index, Column element) {
        map.put(element.getColumnName(), element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends Column> c) {
        c.forEach(e -> map.put(e.getColumnName(), e));
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Column> c) {
        c.forEach(e -> map.put(e.getColumnName(), e));
        return super.addAll(index, c);
    }

    public Column get(String columnName) {
        return map.get(columnName);
    }

    @Override
    public Column get(int index) {
        return super.get(index);
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" | ");
		for(Column curr : this) {
			builder.append(curr.toString());
			builder.append(" | ");
		}
		
		return builder.toString();
	}
}