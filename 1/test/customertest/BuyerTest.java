package customertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import common.GlobalConstants;
import customer.Buyer;
import customer.InvalidInputException;
import property.DateTime;
import property.Offer;
import property.SaleProperty;
import property.SalePropertyException;
import useradministration.UserException;

public class BuyerTest {
	Buyer buyer;
	SaleProperty saleProperty;
	Offer offer;
	DateTime now = new DateTime();
	
	@Before
	public void createObjects() throws UserException {
		buyer = new Buyer("1", "bala123", "aditya", "aditya@customer.serealestate.com", 'B');
		offer = new Offer("1",now , 200.5, GlobalConstants.OFFER_ON_HOLD);
		saleProperty = new SaleProperty("u1,15 d street","CBD", 3, 2, 3, 50000.0, 4.0, "Independent", "Available");
	}
	
	@Test
	public void testCreatedInstance() {
		assertEquals(buyer instanceof Buyer, true);
	}
	
	@Test (expected = InvalidInputException.class)
	public void testHandleMenuInvalidInput() throws InvalidInputException {
		assertFalse(buyer.handleMenu(9));
	}
	
	@Test
	public void testWithDrawOffer() {
		offer.setOfferStatus('W');
		assertEquals(offer.getOfferStatus(), GlobalConstants.OFFER_WITHDRAWN);
	}
	
	@Test(expected = SalePropertyException.class)
	public void testSoldPropertyBoundryLessThanMinPrice() throws SalePropertyException {
		saleProperty.soldProperty(49999);
	}
	
	@Test
	public void testSoldPropertyValidPrice() throws SalePropertyException {
		saleProperty.soldProperty(50000);
		assertEquals("Sold", saleProperty.getPropertyStatus());
	}
	
	
	@Test
	public void testSoldPropertyInspectionStatus() throws SalePropertyException {
		saleProperty.soldProperty(50000);
		assertEquals("Not Available", saleProperty.getInspectionStatus());
	}
}
