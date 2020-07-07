package customertest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import common.GlobalConstants;
import customer.Customer;
import customer.InvalidInputException;
import customer.Landlord;
import customer.LandlordException;
import customer.Tenant;
import property.Application;
import property.DateTime;
import property.RentalProperty;
import useradministration.UserException;

public class LandlordTest {

	Landlord customerLandlord;
	RentalProperty independentProperty;
	Customer customerTenant;
	Application application;
	DateTime now = new DateTime();
	

	@Before
	public void createObjects() throws UserException {
		customerLandlord = new Landlord("2", "bala123", "aditya", "aditya@customer.serealestate.com",
				GlobalConstants.LANDLORD);
		independentProperty = new RentalProperty("u1,15 d street", "CBD", 3, 2, 3, "Independent", "Available", 300.0,
				4.0);
		customerTenant = new Tenant("3", "Summet1", "sumeet", "sumeet@customer.serealestate.com",
				GlobalConstants.TENANT);
		application = new Application(35, 4, 1, now, GlobalConstants.APPLICATION_ON_HOLD);
	}
	
	@Test
	public void testCreatedInstance() {
		assertEquals(customerLandlord instanceof Landlord, true);
	}
	

	@Test (expected = InvalidInputException.class)
	public void testHandleMenuInvalidInput() throws InvalidInputException {
		assertFalse(customerLandlord.handleMenu(11));
	}

	@Test
	public void testAddListProperties() throws LandlordException {
		customerLandlord.addListProperties(independentProperty);
		assertTrue(customerLandlord.getListProperties().containsKey(independentProperty.getPropertyId()));
	}
	
	@Test(expected = LandlordException.class)
	public void testAddListPropertiesDuplicateProperties() throws LandlordException {
		customerLandlord.addListProperties(independentProperty);
		customerLandlord.addListProperties(independentProperty);
		assertTrue(customerLandlord.getListProperties().containsKey(independentProperty.getPropertyId()));
	}
	
	@Test
	public void testUpdateAcceptableContractDuration() throws LandlordException {
		independentProperty.setAcceptableContractDuration(10);
		assertEquals(10, independentProperty.getAcceptableContractDuration());
	}

	@Test
	public void testAcceptApplication()  {
		application.setApplicationStatus("Accepted");
		assertEquals(GlobalConstants.APPLICATION_ACCEPTED, application.getApplicationStatus());
	}	
}
