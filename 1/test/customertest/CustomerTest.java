package customertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import customer.Customer;
import customer.InvalidInputException;
import property.Property;
import useradministration.UserException;

public class CustomerTest {
	Customer customer;
	Property independentProperty;
	
	@Before
	public void createObjects() throws UserException {
		customer = new Customer("1", "bala123", "aditya", "aditya@customer.serealestate.com", 'v');
	}
	

	@Test (expected = InvalidInputException.class)
	public void testHandleMenuInvalidInput() throws InvalidInputException {
		assertFalse(customer.handleMenu(4));
	}
	
	
	@Test
	public void testSetUserType () {
		customer.setUserType('T');
		assertEquals('T', customer.getUserType());
	}
	
	@Test
	public void testSetOccupation () {
		customer.setOccupation("teacher");
		assertEquals(customer.getOccupation(), "teacher");
	}
	
	@Test (expected = InvalidInputException.class)
	public void testCaptureDetailsNullInput () throws InvalidInputException {
		customer.captureDetails(null);
	}
	
	@Test (expected = InvalidInputException.class)
	public void testCaptureDetailsEmptyInput () throws InvalidInputException {
		customer.captureDetails("");
	}
	
	@Test
	public void testGetCustomerId () {
		customer.setCustomerId("cus123");
		assertEquals("cus123", customer.getCustomerId());
	}	
		
	
	@Test
	public void testUserGroup() {
		assertEquals('C', customer.getUserGroup());
	}
	
	@Test
	public void testUpdateUserName() {
		customer.setUserName("bala");
		assertNotEquals("balaaditya", customer.getUserName());
	}	
	
	
	@Test
	public void testUpdateEmployeeName() {
		customer.setEmployerName("google");
		assertEquals("google", customer.getEmployerName());
	}
}
