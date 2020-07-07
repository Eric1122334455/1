package employeetest;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import employee.BranchAdmin;

public class BranchAdminTest {
	BranchAdmin obj;
		
	@Before
	public void intialisation() throws Exception 
	{
		BranchAdmin obj;
		
	}	
		
	@Test 
	public void testEmployeeDetails() throws Exception 
	{
		
		obj=new BranchAdmin( "JK", "jk@sec.com", "BRANCH ADMIN", "FULLTIME");
//		System.out.println(obj);
		boolean valid=obj.employeePayroll("BRANCH MANAGER","FULLTIME");
		assertTrue(valid);
		
	}		
}
