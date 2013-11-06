package mysql;

public class IlegalQueryException extends Exception
{
	private static final long serialVersionUID = -4558850917607742767L;

	public IlegalQueryException(String msg)
	{
		super(msg);
	}
}
