package customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import common.CustomerConstants;
import common.SERealEstateAgency;
import dataaccess.DataAccessException;
import dataaccess.DataAccessLayer;
import dataaccess.PropertySearch;
import useradministration.User;
import useradministration.UserException;

public class Customer extends User {

	private char userType;
	private String customerId;
	private String cutsomerType;

	public String generateUserId() {
		String generatedUserId;
		Random random = new Random();
		int randomId;
		try {
			DataAccessLayer dal = new DataAccessLayer();
			do {
				randomId = (random.nextInt(900) + 100);
				generatedUserId = CustomerConstants.USERID_PREFIX + this.getUserType() + randomId;
			} while (dal.checkUserExists(generatedUserId));
			return generatedUserId;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Customer(String userId, String userPassword, String userName, String userEmail, char userType)
			throws UserException {
		super(userId, userPassword, userName, userEmail);
		this.userType = userType;
		this.setUserGroup();
	}

	public void setUserGroup() {
		super.setUserGroup(CUSTOMER);
	}

	public char getUserType() {
		return this.userType;
	}

	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	SERealEstateAgency se = new SERealEstateAgency();
	private double Income;
	private String occupation;
	private String rentalContractRef;
	private int rentHistoryMonths;
	private String employerName;
	private int employmentMonths;

	public double getIncome() {
		return Income;
	}

	public void setIncome(double income) {
		Income = income;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getRentalContractRef() {
		return rentalContractRef;
	}

	public void setRentalContractRef(String rentalContractRef) {
		this.rentalContractRef = rentalContractRef;
	}

	public int getRentHistoryMonths() {
		return rentHistoryMonths;
	}

	public void setRentHistoryMonths(int rentHistoryMonths) {
		this.rentHistoryMonths = rentHistoryMonths;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public int getEmploymentMonths() {
		return employmentMonths;
	}

	public void setEmploymentMonths(int employmentMonths) {
		this.employmentMonths = employmentMonths;
	}

	@Override
	public boolean displayMenu() {
		System.out.println("Search Properties: 1\nView profile: 2\nUpdate profile: 3\nEnter your choice:");
		boolean returnval = true;

		try {
			returnval = this.handleMenu(1);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnval;
	}

	public boolean handleMenu(int choice) throws InvalidInputException {
		boolean returnval = true;

		try {
			System.out.println("====================================");
			System.out.println("\nYou have selected " + choice + "\r");
			switch (choice) {
			case 1:
				char type = captureDetails("type of property, [S]ale  or [R]ent").toCharArray()[0];
				new PropertySearch(type);
				break;

			case 2:
				displayProfile();
				// se.showMainMenu();
				break;

			case 3:
				updateProfile();
				// se.showMainMenu();
				break;

			default:
				throw new InvalidInputException(CustomerConstants.INVALID_SELECTION + ", please try again\r\n");
			}
		} catch (Exception e) {
			returnval = false;
			throw new InvalidInputException(e.getMessage());
			// se.showMainMenu();

		}

		return returnval;

	}

	public void setUserType(char userType) {
		this.userType = userType;
	}

	public void displayProfile() {
		System.out.println("customer name:" + getUserName());
		System.out.println("customer id:" + getUserId());
		System.out.println("email: " + getUserEmail());
	}

	public void updateProfile() throws InvalidInputException {
		System.out.println("select the field to be updated\n username : 1\n email : 2\n passsword 3");
		try {
			int choice = Integer.parseInt(br.readLine());
			System.out.println("\nYou have selected " + choice + "\r");
			DataAccessLayer dal = new DataAccessLayer();
			switch (choice) {
			case 1:
				System.out.println("Enter username:");
				String userName = br.readLine();
				if (userName != null) {
					this.setUserName(userName);
					dal.saveUser(this);
				} else {
					throw new InvalidInputException("Enter a valid username");
				}

			case 2:
				System.out.println("Enter email:");
				String email = br.readLine();
				if (email != null) {
					this.setUserEmail(email);
					dal.saveUser(this);
				} else {
					throw new InvalidInputException("Enter a valid email");
				}

			case 3:
				System.out.println("Enter new password:");
				String password = br.readLine();
				if (password != null) {
					System.out.println("ReEnter new password:");
					String confPassword = br.readLine();
					if (password.equals(confPassword)) {
						this.setUserPassword(password);
						dal.saveUser(this);
					} else {
						throw new InvalidInputException("Enter a valid password");
					}
				} else {
					throw new InvalidInputException("Enter a valid password");
				}

			default:
				throw new InvalidInputException(CustomerConstants.INVALID_SELECTION + ", please try again\r\n");
			}
		} catch (Exception e) {
			throw new InvalidInputException("You have made an invalid selection");
		}
	}

	public String captureDetails(String message) throws InvalidInputException {
		if (message == null || message.isEmpty()) {
			throw new InvalidInputException("invalid message passed");
		}
		System.out.print("\n Enter " + message + ":");
		try {
			String value = br.readLine();
			if (value != null) {
				return value;
			} else {
				throw new InvalidInputException("invalid input provided");
			}
		} catch (IOException e) {
			throw new InvalidInputException("invalid input provided");
		}
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCutsomerType() {
		return cutsomerType;
	}

	public void setCutsomerType(String cutsomerType) {
		this.cutsomerType = cutsomerType;
	}
}
