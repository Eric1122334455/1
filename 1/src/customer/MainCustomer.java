package customer;

import common.GlobalConstants;
import useradministration.User;

public class MainCustomer  {
	public static void main(String[] args) {
		try {
			User cus = new Landlord("1", "123", "hai", "sas@gs.com", GlobalConstants.LANDLORD);
			cus.displayMenu();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	
}
