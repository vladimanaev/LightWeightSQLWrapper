package mysql;

/**
 * This class serve as column in MySQL database
 * 
 * @author Vladi - 01:59 PM 9/12/2013
 */
public class DataBaseColumn
{
	private String columnName;
	private String columnValue;

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getColumnValue()
	{
		return columnValue;
	}

	public void setColumnValue(String columnValue)
	{
		this.columnValue = columnValue;
	}
}