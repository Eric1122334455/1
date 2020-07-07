package property;

import property.DateTime;

public class Offer {
	private String CustomerID;
	private DateTime OfferDate;
	private double offerAmount;
	private char offerStatus;
	private String offerID;
	private String propertyId;
	
	public Offer() {} 

	public Offer(String customerID, DateTime offerDate, double offerAmount, char offerStatus) {
		CustomerID = customerID;
		OfferDate = offerDate;
		this.offerAmount = offerAmount;
		this.offerStatus = offerStatus;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public DateTime getOfferDate() {
		return OfferDate;
	}

	public void setOfferDate(DateTime offerDate) {
		OfferDate = offerDate;
	}

	public double getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(double offerAmount) {
		this.offerAmount = offerAmount;
	}

	public char getOfferStatus() {
		return offerStatus;
	}

	public void setOfferStatus(char offerStatus) {
		this.offerStatus = offerStatus;
	}

	public String getOfferID() {
		return offerID;
	}

	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	
}
