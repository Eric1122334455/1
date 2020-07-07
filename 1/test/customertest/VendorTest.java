package customertest;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import common.GlobalConstants;
import customer.InvalidInputException;
import customer.Vendor;
import customer.VendorException;
import property.DateTime;
import property.Offer;
import property.SaleProperty;
import useradministration.UserException;

public class VendorTest {
	Vendor customerVendor;
	SaleProperty independentProperty;
	Offer offer;
	DateTime now = new DateTime();
	
	@Before
	public void createObjects() throws UserException {
		customerVendor = new Vendor("2","bala123", "aditya", "aditya@customer.serealestate.com", GlobalConstants.VENDOR);
		independentProperty = new SaleProperty("u1,15 d street","CBD", 3, 2, 3, 50000.0, 4.0, "Independent", "Available");
		offer = new Offer("1",now , 200.5, GlobalConstants.OFFER_ON_HOLD);
	}
	
	@Test
	public void testCreatedInstance() {
		assertEquals(customerVendor instanceof Vendor, true);
	}
	
//	@Test (expected = InvalidInputException.class)
//	public void testHandleMenuInvalidInput() throws InvalidInputException {
//		assertFalse(customerVendor.handleMenu(4));
	//}
	
	@Test (expected = VendorException.class)
	public void testaddListPropertiesInvalidProperty() throws VendorException {
		SaleProperty invalidProperty = null;
		customerVendor.addListProperties(invalidProperty);
	}
	
	
	@Test
	public void testRejectOffer() {
		offer.setOfferStatus('R');
		assertEquals(offer.getOfferStatus(), GlobalConstants.OFFER_REJECTED);
	}
	
	@Test
	public void testAcceptOffer() {
		offer.setOfferStatus('A');
		assertEquals(offer.getOfferStatus(), GlobalConstants.OFFER_ACCEPTED);
	}
	
	@Test
	public void testUpdateMinimumPrice() {
		independentProperty.setMinimumPrice(2545);
		assertNotEquals(50000, independentProperty.getMinimumPrice());
	}
	
	
	@Test
	public void testNegotaiteComission() {
		independentProperty.setSalesCommision(6);
		assertEquals(6, independentProperty.getSalesCommision());
	}
}
