package mysql;

import java.util.ArrayList;

/**
 * This class should serve as Result from MySQL database query
 * 
 * @author Vladi - 07:30 PM 9/12/2013
 */
public class Result
{
	private ArrayList<DataBaseRow> rows = new ArrayList<DataBaseRow>();

	public DataBaseRow getRow(int index)
	{
		return this.rows.get(index);
	}

	public void addRow(DataBaseRow dataBaseRow)
	{
		this.rows.add(dataBaseRow);
	}

	/**
	 * Returns number of rows
	 * 
	 * @return number of rows
	 */
	public int size()
	{
		return this.rows.size();
	}

	/**
	 * Remove all rows
	 */
	public void clear()
	{
		this.rows.clear();
	}
}