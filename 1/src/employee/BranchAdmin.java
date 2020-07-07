package employee;

public class BranchAdmin extends Employee {

	public int netpay = 0;
	public int netSalaryAmount;
	public String designation;
	public BranchAdmin(String employeeName, String emailId, String designation, String employeeType) {
		super(employeeName, emailId, designation, employeeType, 'A');

	}
	
	public int intiatePayRoll() {
		return netSalaryAmount;
	}

	public boolean employeePayroll(String designation, String employeeType ) throws BranchAdminException
    {	
       this.designation = designation; 	
    	if(designation.equalsIgnoreCase("SALES CONSULTANT")||designation.equalsIgnoreCase("PROPERTY MANAGER")
    			|| designation.equalsIgnoreCase("BRANCH MANAGER"))
		{			
			if(employeeType.equalsIgnoreCase("PARTTIME"))
			{
				netpay=4000;
				
				System.out.println(designation.toString()+ " part-time salary is "+ netpay);
				return true;
				
			}
			
			else if(employeeType.equalsIgnoreCase("FULLTIME"))
			{	
				netpay=8000;
				System.out.println(designation.toString()+ " full-time salary is "+ netpay);
				return true;
							
			}
			
			else {
				System.out.println("Invalid Employee-type");
				return false;
			}
		}		
    	else {
    		System.out.println("Invalid Designation");
    	}
		
    	return false;    			
		
    }

}