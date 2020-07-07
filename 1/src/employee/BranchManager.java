package employee;
import java.util.Scanner;


public class BranchManager extends Employee{
	
	public int netHours = 0;
	public BranchManager(String employeeName, String emailId, String designation, String employeeType) {
		super(employeeName, emailId, designation, employeeType);

	}
	public boolean approvePartTimeHours(String eid, String designation, String employeeType, int netHours) {
		if(designation.equalsIgnoreCase("SALES CONSULTANT")||designation.equalsIgnoreCase("PROPERTY MANAGER"))
		{			
			if(employeeType.equalsIgnoreCase("PARTTIME"))
			{
				if(netHours <= 120) {
					System.out.println("Employee "+eid + ", your work-hours got Approved");
					return true;
				}
				System.out.println("Not Approved \nPart-time Employees are not allowed to work more than 120 hours monthly.");
				return false;
			}
			System.out.println("Only part-time employee(s) can request for work-hours approval.");
			return false;
		}
		
		System.out.println("Invalid Designation");
		return false;
	}
	
	public boolean addInspection(String propertyID, String inspectDateTime) {
		return true;
				
	}
}
