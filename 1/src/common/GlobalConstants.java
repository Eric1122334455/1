package common;

public interface GlobalConstants {
	
	// constants for User Group
	public static final char	PUBLIC		= 'P';
	public static final char	CUSTOMER	= 'C';
	public static final char	EMPLOYEE	= 'E';
	

	// constants for User Type (Preference)
	// applicable for both CUSTOMER and EMPLOYEE
	public static final char	UNDEFINED	= 'U';

	// applicable for User Group CUSTOMER
	public static final char	BUY			= 'B';
	public static final char	SELL		= 'V';
	public static final char	RENT		= 'T';
	public static final char	LEASE		= 'L';
	
	public static final char	CUSTOMERTYPELIST[] = new char[] {BUY, SELL, RENT, LEASE};	
	
	// property types
	public static final char	RENTAL		= 'R';
	public static final char	SALE		= 'S';
	
	// applicable for User Group EMPLOYEE
	public static final char	BRANCHMANAGER	= 'B';
	public static final char	BRANCHADMIN		= 'A';

	public static final char	SALESCONSULTANT	= 'S';
	public static final char	PROPERTYMANAGER	= 'P';
	
	public static final char	EMPLOYEETYPELIST[] = new char[] {BRANCHMANAGER, BRANCHADMIN, SALESCONSULTANT, PROPERTYMANAGER};	
	
	// set to true to display system admin menu
	public static final boolean	ADMINMODE	=	false;
		
	public static final String DB_NAME		= "sereal";
	public static final String DB_USER		= "SA"; 
	public static final String DB_PASSWORD	= "";

	// constants for customer type
	public static final char VENDOR			= 'V';
	public static final char LANDLORD		= 'L';
	public static final char BUYER			= 'B';
	public static final char TENANT			= 'T';

	// constants for property
	public static final String PROPERTY_AVAILABLE		= "Available";
	public static final String PROPERTY_NOT_AVAILABLE	= "NotAvailable";

	// constants for application
	public static final String APPLICATION_ACCEPTED		= "Accepted";
	public static final String APPLICATION_REJECTED		= "Rejected";
	public static final String APPLICATION_ON_HOLD		= "Hold";
	public static final String APPLICATION_WITHDRAWN	= "Withdrawn";

	// constants for offer
	public static final char OFFER_ACCEPTED		= 'A';
	public static final char OFFER_REJECTED 	= 'R';
	public static final char OFFER_ON_HOLD		= 'H';
	public static final char OFFER_WITHDRAWN	= 'W';
}
