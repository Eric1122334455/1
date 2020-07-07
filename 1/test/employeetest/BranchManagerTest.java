package employeetest;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import employee.BranchManager;

class BranchManagerTest {

	@Test
	void test() {
		
	BranchManager ab=new BranchManager(null, null, null, null);
	Boolean exp= ab.approvePartTimeHours("E101","PROPERTY MANAGER","PARTTIM",120);
	assertTrue(exp);
	
	}

}
