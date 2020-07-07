package customer;

public class TenantException extends Exception {
	private static final long serialVersionUID = 1L;

	public TenantException(String message) {
		super(message);
	}

}
