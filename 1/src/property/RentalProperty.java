package property;

import java.util.HashMap;
import java.util.Scanner;

public class RentalProperty extends Property {

	private double expecweeklyRentalRate;
	private double acceptableContractDuration;
	private String inspectionStatus;
	private Double manaagementFee;
	public Double getManaagementFee() {
		return manaagementFee;
	}

	public void setManaagementFee(Double manaagementFee) {
		this.manaagementFee = manaagementFee;
	}

	// private ArrayList<RentalProperty> rep = new ArrayList<RentalProperty>();
	Scanner keyboard = new Scanner(System.in);
	private HashMap<String, Application> applications = new HashMap<>();
	private HashMap<String, RentalProperty> listedRentalProperties = new HashMap<>();

	public HashMap<String, RentalProperty> getListedRentalProperties() {
		return listedRentalProperties;
	}

	public void setListedRentalProperties(HashMap<String, RentalProperty> listedRentalProperties) {
		this.listedRentalProperties = listedRentalProperties;
	}

	public RentalProperty(String propertyAddress, String suburb, int numbOfBedrooms, int numbOfBathooms,
			int numbOfCarSpace, String propertyType, String propertystatus, double expecweeklyRentalRate,
			double acceptableContractDuration) {
		super(propertyAddress, suburb, numbOfBedrooms, numbOfBathooms, numbOfCarSpace, propertyType, propertystatus);

		this.expecweeklyRentalRate = expecweeklyRentalRate;
		this.acceptableContractDuration = acceptableContractDuration;
	}

	public RentalProperty() {
		// TODO Auto-generated constructor stub
	}

	public double getWeeklyRentalRate() {
		return expecweeklyRentalRate;
	}

	public void setWeeklyRentalRate(double weeklyRentalRate) {
		this.expecweeklyRentalRate = weeklyRentalRate;
	}

	public double getAcceptableContractDuration() {
		return acceptableContractDuration;
	}

	public void setAcceptableContractDuration(double acceptableContractDuration) {
		this.acceptableContractDuration = acceptableContractDuration;
	}

	public double managementFeeforSingleProperty(double weeklyRentalrate) {
		double managementfee = ((weeklyRentalrate * 4) * 8) / 100;
		return managementfee;

	}

	public void managementFeeforMultipleProperty(int discountrate) {
		System.out.println("Please Enter The Total Number of Property That you have");
		int n = keyboard.nextInt();
		for (int i = 1; i <= n; i++) {
			System.out.println("Please Enter Weekly Rental Rate for Each of your property: ");
			double k = keyboard.nextInt();
			double totalweeklyRate = k + 1;

			double managementfee = ((totalweeklyRate * 4) * 7) / 100;
			System.out.println("Total Management Fee For Your Property is " + managementfee);

		}

	}

	public double negotiatedmanagementFeeforSingleProperty(double weeklyRentalrate) {
		double managementfee = ((weeklyRentalrate * 4) * 7) / 100;
		return managementfee;
	}

	public void negotiatedmanagementFeeforMultipleProperty(int discountrate) {
		System.out.println("Please Enter The Total Number of Property That you have");
		int n = keyboard.nextInt();
		for (int i = 1; i <= n; i++) {
			System.out.println("Please Enter Weekly Rental Rate for Each of your property: ");
			double k = keyboard.nextInt();
			double totalweeklyRate = k + 1;

			double managementfee = ((totalweeklyRate * 4) * 6) / 100;
			System.out.println("Total Management Fee For Your Property is " + managementfee);

		}

	}

	public double getExpecweeklyRentalRate() {
		return expecweeklyRentalRate;
	}

	public void setExpecweeklyRentalRate(double expecweeklyRentalRate) {
		this.expecweeklyRentalRate = expecweeklyRentalRate;
	}

	public boolean acceptRentalOffer(double weeklyRentalRate, DateTime date) {

		if (weeklyRentalRate >= expecweeklyRentalRate) {
			System.out.println("OFFER IS WAITING FOR Landlords RESPONSE");

			System.out.println(" Press '1' to accpet the offer or '2' to reject the offer");
			int choice = keyboard.nextInt();

			DateTime toda = new DateTime();

			if (choice == 1 && DateTime.diffDays(toda, date) <= 3) {

				System.out.println("CONGRATULATIONS !YOUR OFFER IS ACCEPTED BY VENDOR");
				setInspectionStatus("Available");
				setPropertyStatus("OfferAccepted");

			}

			else if (choice == 2 && DateTime.diffDays(toda, date) <= 3) {
				System.out.println("Offer is Rejected BY LandLord");
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

	public boolean confirmRentalOffer(double downPayment, DateTime date1) {

		DateTime current = new DateTime();
		double approxdwnPayment = downPayment++;

		if (approxdwnPayment >= downPayment && DateTime.diffDays(current, date1) <= 1) {
			setInspectionStatus("ALL Inspections canceled");
			setPropertyStatus("let");
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

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public HashMap<String, Application> getApplications() {
		return applications;
	}

	public void setApplications(HashMap<String, Application> applications) {
		this.applications = applications;
	}

	

}