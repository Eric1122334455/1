package property;

import customer.InvalidInputException;

public abstract class Property {

	private String propertyAddress;
	private String suburb;
	private int numbOfBedrooms;
	private int numbOfBathrooms;
	private int numbOfCarSpace;
	private String propertyType;
	private String propertyStatus;
	private String propertyId;

	public String getPropertyId() {
		return propertyId;
	}

	public Property() {
		
	}
	
	public Property(String propertyAddress, String suburb, int numbOfBedrooms, int numbOfBathrooms, int numbOfCarSpace, String propertyType, String propertyStatus) {
		this.propertyAddress = propertyAddress;
		this.suburb = suburb;
		this.numbOfBedrooms = numbOfBedrooms;
		this.numbOfBathrooms = numbOfBathrooms;
		this.numbOfCarSpace = numbOfCarSpace;
		this.propertyType = propertyType;
		this.propertyStatus = propertyStatus;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public int getNumbOfBedrooms() {
		return numbOfBedrooms;
	}

	public void setNumbOfBedrooms(int numbOfBedrooms) {
		this.numbOfBedrooms = numbOfBedrooms;
	}

	public int getNumbOfBathrooms() {
		return numbOfBathrooms;
	}

	public void setNumbOfBathrooms(int numbOfBathrooms) {
		this.numbOfBathrooms = numbOfBathrooms;
	}

	public int getNumbOfCarSpace() {
		return numbOfCarSpace;
	}

	public void setNumbOfCarSpace(int numbOfCarSpcae) {
		this.numbOfCarSpace = numbOfCarSpcae;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyStatus() {
		return propertyStatus;
	}

	public void setPropertyStatus(String propertyStatus) {
		this.propertyStatus = propertyStatus;
	}

	public void setPropertyId(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setSection32(char charAt) {
		// TODO Auto-generated method stub
		
	}

	public void setApproved(char charAt) {
		// TODO Auto-generated method stub
		
	}
}
