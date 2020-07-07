package useradministration;
import java.util.Scanner;

import common.GlobalConstants;
import customer.Customer;
import employee.Employee;
import dataaccess.*;

public class UserAccessControl implements GlobalConstants{

	Scanner console = new Scanner(System.in);
	
	public UserAccessControl() {
	}

	// register a user (customer)
	public User registerUser() throws UserException, ClassNotFoundException {

		User u = null;

		try {
			System.out.println("Register a new user");
			System.out.println("-------------------");
	
			String	name	= this.inputDetail("Name");
			String	eMail	= this.inputDetail("eMail");
			String	password= this.inputDetail("Password");
			
			char	customerType= this.inputDetail("interest [B]uy, [S]ell, [R]ent or [L]ease", CUSTOMERTYPELIST);
			
			double	income				= 0;

			int		rentHistoryMonths	= 0;
			int		employmentMonths	= 0;

			String	occupation			= null;
			String	rentalContractRef	= null;
			String	employerName		= null;
			
			// when registering a renter, we need to capture more details
			if (customerType == RENT) {
				
				income				= Double.parseDouble(this.inputDetail("income"));
				occupation			= this.inputDetail("occupation");
				rentHistoryMonths	= Integer.parseInt(this.inputDetail("rent months"));
				rentalContractRef	= this.inputDetail("rental contract reference");
				employerName		= this.inputDetail("employer name");
				employmentMonths	= Integer.parseInt(this.inputDetail("employment months"));
			}
			
			DataAccessLayer dal = new DataAccessLayer();
			
			String	userId = dal.genNewId("user");
			
			u = this.createCustomer(userId, password, name, eMail, customerType, income, occupation, rentHistoryMonths,
					rentalContractRef, employerName, employmentMonths);

			System.out.println("New user created with Id:" + u.getUserId());

		}
		
		catch (DataAccessException dae) {
			throw new UserException(dae.getMessage());
		}
		
		catch (UserException ue) {
			throw new UserException(ue.getMessage());
		}
		
		return u;

	}
	

	// login a user
	public User loginUser() throws UserException, ClassNotFoundException {
		
		String	userId	= this.inputDetail("User Id");
		String	password= this.inputDetail("Password");
		
		User u = null;

		try {
			this.verifyLogin(userId, password);
		}

		catch (UserException ue) {
			throw new UserException(ue.getMessage());
		}		
		
		try {

			DataAccessLayer dal = new DataAccessLayer();

			u = dal.getUserDetails(userId);
		}
		
		catch (DataAccessException dae) {
			throw new UserException(dae.getMessage());
		}
		
		System.out.println("Currently logged in as: " + userId);
		
		return u;
	}

	
	// method will create and save a user in Customer Group
	public Customer createCustomer( String userId, String password, String name, 
									String eMail, char userType, double income, 
									String occupation, int rentHistoryMonths, 
									String rentalContractRef, String employerName, 
									int employmentMonths) throws UserException {
		
		Customer c = null;

		// create user
		c = new Customer(userId, password, name, eMail, userType);
		
		c.setIncome(income);
		c.setOccupation(occupation);
		c.setRentHistoryMonths(rentHistoryMonths);
		c.setRentalContractRef(rentalContractRef);
		c.setEmployerName(employerName);
		c.setEmploymentMonths(employmentMonths);

		// save user
		try {
			c.saveUser();
		}
		
		catch (ClassNotFoundException | UserException ue) {
			throw new UserException(ue.getMessage());
		}
		
		return c;
	}

	
	// method will create and save a user in Employee Group
	public Employee createEmployee(String userId, String password, String name, 
										String eMail, char role) throws UserException, ClassNotFoundException {
		
		Employee e = null;

		// create user
		e = new Employee(userId, password, name, eMail, role);

		// save user
		try {
			e.saveUser();
		}
		
		catch (UserException ue) {
			throw new UserException(ue.getMessage());
		}
		
		return e;
	}


	public boolean verifyLogin(String userId, String password) throws UserException {

		boolean returnVal = false;
		
		try {
			DataAccessLayer dal = new DataAccessLayer();
			dal.verifyLogin(userId, password);
			returnVal = true;
		}

		catch (DataAccessException e) {
			returnVal = false;
			throw new UserException(e.getMessage());
		}
		
		return returnVal;
	}
	
	
	private String inputDetail(String detail) {
		
		String returnVal = "";
		
		System.out.println("Enter " + detail + ":");
		
		returnVal = console.nextLine();
		
		return returnVal;
		
	}
	

	private char inputDetail(String detail, char validChar[]) {
		
		char inputChar = this.inputDetail(detail).charAt(0);
		char returnVal = 0;
		
		// validate details
		for (int i = 0; i < validChar.length; i++) {
			if (inputChar == validChar[i]) {
				returnVal = inputChar;
				break;				
			}
		}
		
		return returnVal;
	}
}