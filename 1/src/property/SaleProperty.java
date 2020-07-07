package property;

import java.util.HashMap;
import java.util.Scanner;
import property.DateTime;

public class SaleProperty extends Property {
	private double salesCommision;
	private double minimumPrice;
	private String inspectionStatus;
	Scanner keyboard = new Scanner(System.in);
	private HashMap<String, Offer> offers = new HashMap<>();
	private HashMap<String, SaleProperty> listedSaleProperties = new HashMap<>();

	public SaleProperty(String propertyAddress, String suburb, int numbOfBedrooms, int numbOfBathooms,
			int numbOfCarSpace, double minimumPrice, double salesCommision, String propertyType,
			String propertyStatus) {
		super(propertyAddress, suburb, numbOfBedrooms, numbOfBathooms, numbOfCarSpace, propertyType,
				propertyStatus);
		// TODO Auto-generated constructor stub
		this.salesCommision = salesCommision;
		this.minimumPrice = minimumPrice;
	}

	public SaleProperty() {
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, Offer> getOffers() {
		return offers;
	}

	public void setOffers(HashMap<String, Offer> offers) {
		this.offers = offers;
	}

	public HashMap<String, SaleProperty> getListedSaleProperties() {
		return listedSaleProperties;
	}

	public void setListedSaleProperties(HashMap<String, SaleProperty> listedSaleProperties) {
		this.listedSaleProperties = listedSaleProperties;
	}

	public double getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(double minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public double getSalesCommision() {
		return salesCommision;
	}

	public void setSalesCommision(double salesCommision) {
		this.salesCommision = salesCommision;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public boolean acceptSalesOffer(double offeredPrice, DateTime date) {

		if (offeredPrice >= minimumPrice) {
			System.out.println("OFFER IS WAITING FOR VENDORS RESPONSE");

			System.out.println(" Press '1' to accpet the offer or '2' to reject the offer");
			int choice = keyboard.nextInt();

			DateTime toda = new DateTime();

			if (choice == 1 && DateTime.diffDays(toda, date) <= 3) {

				System.out.println("CONGRRATULATIONS !YOUR OFFER IS ACCEPTED BY VENDOR");
				setInspectionStatus("Available");
				setPropertyStatus("OfferAccepted");

			}

			else if (choice == 2 && DateTime.diffDays(toda, date) <= 3) {
				System.out.println("Offer is Rejected BY VENDOR");
			} else {
				System.out.println("OFFER IS REJECTED BY SYSTEM AUTOMATICALLY");
			}

		}

		else {
			System.out.println("Offer is rejected due to insufficient PRICE");
			setInspectionStatus("Available");
			setPropertyStatus("Available");

		}
		return true;

	}

	public boolean confirmSalesOffer(double downPayment, DateTime date1) {

		DateTime current = new DateTime();
		double approxdwnPayment = (getMinimumPrice() * 10) / 100;

		if (approxdwnPayment >= downPayment && DateTime.diffDays(current, date1) <= 1) {
			setInspectionStatus("ALL Inspections canceled");
			setPropertyStatus("Under Contract");
		} else if (approxdwnPayment <= downPayment && DateTime.diffDays(current, date1) <= 1) {
			setInspectionStatus("Available");
			setPropertyStatus("Available");
		}

		else {
			setInspectionStatus("Available");
			setPropertyStatus("Available");
		}
		return true;

	}

	public void soldProperty(double fullpayment) throws SalePropertyException {
		double pay = getMinimumPrice();
		if (fullpayment >= pay) {
			setInspectionStatus("Not Available");
			setPropertyStatus("Sold");
		} else {
			throw new SalePropertyException("You will have to pay full price to Buy The Property");
		}

	}
}
