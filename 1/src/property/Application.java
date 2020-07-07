package property;

import java.util.HashMap;

import customer.Tenant;

public class Application {

	public String getPropertyId() {
		return propertyId;
	}


	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	private double weeklyRentalAmount;
	private int contractduration;
	private int noOfApplications;
	private String applicationStatus;
	private HashMap<String, Tenant> applicants = new HashMap<String, Tenant>();
	private DateTime applicationDate;
	private String applicationId;
	private String propertyId;
	
	
	public String getApplicationId() {
		return applicationId;
	}


	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


	public Application(double weeklyRentalAmount, int contractduration, int noOfApplicants, DateTime applicationDate, String appliationStatus) {
		super();
		this.weeklyRentalAmount = weeklyRentalAmount;
		this.contractduration = contractduration;
		this.noOfApplications = noOfApplicants;
		this.applicationDate = applicationDate;
		this.applicationStatus = appliationStatus;
		this.applicationDate= applicationDate;
	}

	
	public Application() {
		// TODO Auto-generated constructor stub
	}


	public int getNoOfApplications() {
		return noOfApplications;
	}

	public void setNoOfApplications(int noOfApplications) {
		this.noOfApplications = noOfApplications;
	}


	public String getApplicationStatus() {
		return applicationStatus;
	}


	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}


	public HashMap<String, Tenant> getApplicants() {
		return applicants;
	}

	public void setApplicants(String userId, Tenant tenant) {
		this.applicants.put(userId, tenant);
	}

	
	public double getWeeklyRentalAmount() {
		return weeklyRentalAmount;
	}

	public void setWeeklyRentalAmount(double weeklyRentalAmount) {
		this.weeklyRentalAmount = weeklyRentalAmount;
	}

	public int getContractduration() {
		return contractduration;
	}

	public void setContractduration(int contractduration) {
		this.contractduration = contractduration;
	}

	public DateTime getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(DateTime applicationDate) {
		this.applicationDate = applicationDate;
	}

}