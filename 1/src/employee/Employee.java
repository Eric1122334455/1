package employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import dataaccess.PropertySearch;
//import java.util.ArrayList;
//import java.util.HashMap;
import useradministration.User;

public class Employee extends User {

	// VARIABLES TO STORE DETAILS OF AN EMPLOYEE
	public String employeeName;
	public String email;
	public String employeeType;
	public String designation;
	public String eid;
	public String offerID;
	public String propertyID;
	public static float hrs;

//	public Employee(String employeeName, String email, String designation, String employeeType)
//	{super( empPassword, empName,"");
//		
//		this.employeeName = employeeName;
//		this.email = email;
//		this.employeeType = employeeType;
//		this.designation = designation;
//		//this.hrs=hrs;
//		
//	}	

	public Employee(String userId, String empPassword, String empName, String string, char empRole) {
		super(userId, empPassword, empName, "");
		this.employeeName = employeeName;
		this.email = email;
		this.employeeType = employeeType;
		this.designation = designation;
	}

	public int generateID() {
		int randNum;
		Random rand = new Random();
		randNum = rand.nextInt(100);
		return randNum;
	}

	public String getEid() {
		return eid;
	}

	public String getSalesDetails(String propertyID) {
		this.propertyID = propertyID;
		return offerID;
	}

	public String setEid(String eid) {
		return this.eid = eid;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getemployeeType() {
		return employeeType;
	}

	public void setemployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String toString() {

		return "Employee " + employeeName + "\nemail " + email;
	}
//METHOD TO GET THE DETAILS OF THE EMPLOYEE

	public String getDetails() {

		if (employeeType.toUpperCase().equals("FULL TIME")) {
			return "Employee name" + ":" + employeeName + "\n " + "Employee email" + ":" + email + "\n"
					+ "Employee type" + " :" + employeeType + "\n" + " Employee address" + " :" + "\n" + " Employee id"
					+ ":" + eid + "\n ";
		} else {
			return "Employee name" + ":" + employeeName + "\n " + "Employee email" + ":" + email + "\n"
					+ "Employee type" + " :" + employeeType + "\n" + " Employee id" + ":" + eid + "\n" + "Hours Worked"
					+ ":" + hrs;

		}

	}

	public void displayMenu() {
		System.out.println("Search Properties: 1\nAssign Employee: 2\nUpdate properties: 3\nEnter your choice:");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		try {
			employeeMenu(choice);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void employeeMenu(int choice) throws InvalidInputException {
		try {
			System.out.println("====================================");
			System.out.println("\nYou have selected " + choice + "\r");
			switch (choice) {
			case 1:
				System.out.println("Search properties");
				char type = captureDetails("type of property, [S]ale  or [R]ent").toCharArray()[0];
				new PropertySearch(type);
				break;

			case 2:
				System.out.println("Assign Employee");
				assignEmployee();
				break;

			case 3:
				System.out.println("Update Properties");
				break;

			default:
				throw new InvalidInputException("Invalid Option, please try again\r\n");
			}
		} catch (Exception e) {
			throw new InvalidInputException("You have made an invalid selection");
		}

	}

	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public String captureDetails(String message) throws InvalidInputException {
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

	public void assignEmployee() {
		System.out.println("Please enter Employee ID of the person to assign ?");
		Scanner input = new Scanner(System.in);
		String eid = input.nextLine();
		System.out.println("Please the property ID to be assigned ?");
		String propertyID = input.nextLine();
		System.out.println(eid + " assigned to work on property " + propertyID);
	}

	public void setPartTime(char empPartTime) {
		// TODO Auto-generated method stub

	}

	public char getEmployeeRole() {
		// TODO Auto-generated method stub
		return 0;
	}

	public char getPartTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}