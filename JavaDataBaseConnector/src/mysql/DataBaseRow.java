package mysql;

import java.util.ArrayList;

/**
 * This class serve as row in MySQL database
 * 
 * @author Vladi - 01:59 PM 9/12/2013
 */
public class DataBaseRow
{
	private ArrayList<DataBaseColumn> row = new ArrayList<DataBaseColumn>();

	public DataBaseColumn getColumn(int index)
	{
		return this.row.get(index);
	}

	public void addColumn(DataBaseColumn dataBaseColumn)
	{
		this.row.add(dataBaseColumn);
	}

	/**
	 * Returns number of columns
	 * 
	 * @return number of columns
	 */
	public int size()
	{
		return this.row.size();
	}

	/**
	 * Remove all columns
	 */
	public void clear()
	{
		this.row.clear();
	}
}