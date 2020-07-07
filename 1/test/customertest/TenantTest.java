package customertest;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import common.GlobalConstants;
import customer.InvalidInputException;
import customer.Landlord;
import customer.Tenant;
import property.Application;
import property.DateTime;
import property.RentalProperty;
import useradministration.UserException;

public class TenantTest {
	Landlord customerLandlord;
	RentalProperty independentProperty;
	Tenant customerTenant;
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
		assertEquals(customerTenant instanceof Tenant, true);
	}

	@Test(expected = InvalidInputException.class)
	public void testHandleMenuInvalidInput() throws InvalidInputException {
		assertFalse(customerTenant.handleMenu(12));
	}

	@Test
	public void testWithdrawApplication() {
		application.setApplicationStatus(GlobalConstants.APPLICATION_WITHDRAWN);
		assertEquals(GlobalConstants.APPLICATION_WITHDRAWN, application.getApplicationStatus());
	}

	@Test
	public void testAcceptRentalOffer() {
		application.setApplicationStatus(GlobalConstants.APPLICATION_ACCEPTED);
		assertEquals(GlobalConstants.APPLICATION_ACCEPTED, application.getApplicationStatus());
	}
	
	@Test
	public void testAddApplicants() {
		HashMap<String, Tenant> applicant = new HashMap<String, Tenant>();
		applicant.put("3", customerTenant);
		application.setApplicants(applicant);
		assertEquals(application.getNoOfApplications(), 1);
	}
	
	

	@Test
	public void testAcceptRentalOfferInvalidWeeklyRentalRate() {
		independentProperty.acceptRentalOffer(209, now);
	}
}
