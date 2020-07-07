package customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import common.CustomerConstants;
import common.GlobalConstants;
import dataaccess.DataAccessException;
import dataaccess.DataAccessLayer;
import dataaccess.PropertySearch;
import property.Application;
import property.DateTime;
import property.RentalProperty;
import useradministration.UserException;

public class Tenant extends Customer {

	private RentalProperty rentProp;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private HashMap<String, RentalProperty> tenantInterstedProperties = new HashMap<>();
	private HashMap<String, Application> tenantApplications = new HashMap<>();

	public Tenant(String userId, String userPassword, String userName, String userEmail, char userType)
			throws UserException {
		super(userId, userPassword, userName, userEmail, userType);
	}

	// used to load the interested properties into hashmap from database
	public void loadTenantInterstedProperties() {
		// tenantInterstedProperties = PropertySearch.
	}

	// used to update the interested properties into database from hashmap
	public void updateTenantInterstedProperties() {
		// tenantInterstedProperties = PropertySearch.
	}

	// used to load the buyer offers into hashmap from database
	public void loadTenantApplications() {
		DataAccessLayer dal;
		try {
			dal = new DataAccessLayer();
			tenantApplications = dal.getApplicationCustomer(this.getUserId());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
	}

	// used to display menu
	@Override
	public boolean displayMenu() {
		try {
			System.out.println("******TENANT MENU*********");
			super.displayMenu();
			System.out.println(
					"View Notifications: 4\nView Applications: 5\nCreate Application: 6\nWithdraw Application:	7\nMake payment: 8\nAdd interested properties 9\nRemove interested properties 10\nExit 11\nEnter your choice:");
			handleMenu();
		} catch (InvalidInputException iie) {
			System.out.println(iie.getMessage());
			displayMenu();
		}
		return false;
	}

	// used to handle menu
	public void handleMenu() throws InvalidInputException {
		try {
			int choice = Integer.parseInt(br.readLine());

			if (choice < 1 || choice > 11) {
				throw new InvalidInputException("You have made an invalid selection, please try again\r\n");
			} else if (choice >= 1 || choice <= 3) {
				super.handleMenu(choice);
			} else if (choice == 11) {
				System.out.println("You have exited the program.\r\n");
				br.close();
				System.exit(1);
			} else {
				System.out.println("====================================");
				System.out.println("\nYou have selected " + choice + "\r");
				switch (choice) {
				case 4:
					viewNotifications();
					break;

				case 5:
					viewApplications();
					displayMenu();
					break;

				case 6:
					createApplication();
					displayMenu();
					break;

				case 7:
					withdrawApplication();
					displayMenu();
					break;

				case 8:
					makePayment();
					displayMenu();
					break;

				case 9:
					addInterestedProperties();
					displayMenu();
					break;
				case 10:
					removeInterestedProperties();
					displayMenu();
					break;
				case 11:
					System.exit(0);

				default:
					throw new InvalidInputException(CustomerConstants.INVALID_SELECTION + ", please try again\r\n");
				}
			}
		} catch (Exception e) {
			throw new InvalidInputException(CustomerConstants.INVALID_SELECTION);
		}
	}

	// used to remove interesting properties
	private void removeInterestedProperties() throws InvalidInputException {
		try {
			String propertyId = captureDetails("property Id");
			PropertySearch ps = new PropertySearch(propertyId);
			loadTenantInterstedProperties();
			if (ps.getSearchResults().containsKey(propertyId) && tenantInterstedProperties.containsKey(propertyId)) {
				tenantInterstedProperties.remove(propertyId);
				updateTenantInterstedProperties();
			} else {
				throw new TenantException("invalid property id provided");
			}
		} catch (TenantException te) {
			throw new InvalidInputException(te.getMessage());
		}
	}

	// used to add interesting properties
	private void addInterestedProperties() throws InvalidInputException {
		try {
			String propertyId = captureDetails("property Id");
			PropertySearch ps = new PropertySearch(propertyId);
			loadTenantInterstedProperties();
			if (ps.getSearchResults().containsKey(propertyId)) {
				RentalProperty prop = (RentalProperty) ps.getSearchResults().get(propertyId);
				tenantInterstedProperties.put(propertyId, prop);
				updateTenantInterstedProperties();
			} else {
				throw new TenantException("invalid property id provided");
			}
		} catch (TenantException te) {
			throw new InvalidInputException(te.getMessage());
		}
	}

	// used to make payment for an application
	private void makePayment() throws InvalidInputException {
		try {
			DataAccessLayer dal = new DataAccessLayer();

			String propertyId = captureDetails("Property Id");
			loadTenantInterstedProperties();
			if (tenantInterstedProperties.containsKey(propertyId)) {
				String applicationId = captureDetails("Application Id");
				loadTenantApplications();
				Application thisApplication = tenantApplications.get(applicationId);
				if (thisApplication != null
						&& thisApplication.getApplicationStatus().equalsIgnoreCase(APPLICATION_ON_HOLD)) {
					double weeklyRentalRate = Double.parseDouble(captureDetails("Weekly Rental Rate"));
					DateTime date = new DateTime();
					rentProp.acceptRentalOffer(weeklyRentalRate, date);
					dal.saveUser(this);
				} else {
					throw new TenantException("no property found with given id");
				}
			} else {
				throw new TenantException("no application found with given id");
			}
		} catch (TenantException te) {
			throw new InvalidInputException(te.getMessage());
		} catch (DataAccessException de) {
			throw new InvalidInputException(de.getMessage());
		}
	}

	// used to withdraw application
	private void withdrawApplication() throws InvalidInputException {
		String applicationId = captureDetails("Application Id");
		loadTenantApplications();
		Application thisApplication = tenantApplications.get(applicationId);
		if (thisApplication != null) {
			thisApplication.setApplicationStatus(GlobalConstants.APPLICATION_WITHDRAWN);
		} else {
			throw new InvalidInputException("no application found with given id");
		}
	}

	// used to create new application
	private void createApplication() throws InvalidInputException {
		try {
			String rePropId = captureDetails("property id");
			HashMap<String, RentalProperty> listedRentalProp = rentProp.getListedRentalProperties();
			RentalProperty appProperty = listedRentalProp.get(rePropId);
			if (appProperty != null) {
				HashMap<String, Application> rentPropApplication = rentProp.getApplications();
				Iterator<Map.Entry<String, Application>> appIterator = rentPropApplication.entrySet().iterator();
				while (appIterator.hasNext()) {
					Map.Entry<String, Application> appEntries = appIterator.next();
					Application application = appEntries.getValue();
					HashMap<String, Tenant> rentPropApplicants = application.getApplicants();
					if (rentPropApplicants.containsKey(this.getUserId())) {
						throw new TenantException(
								"an application already exists with this property, can't create a new application");
					}
				}
				DateTime now = new DateTime();
				Application newApplication = new Application(Double.parseDouble(captureDetails("Weekly rental amount")),
						Integer.parseInt(captureDetails("contract Duration")),
						Integer.parseInt(captureDetails("contract duration")), now,
						GlobalConstants.APPLICATION_ON_HOLD);
				int numberOfApplicants = Integer.parseInt(captureDetails("Number of Applicants"));
				newApplication.setNoOfApplications(numberOfApplicants);
				for (int j = 0; j < numberOfApplicants; j++) {
					String userId = captureDetails("userId");
					DataAccessLayer dal = new DataAccessLayer();
					Tenant applicantObj = (Tenant) dal.getCustomerDetails(userId);
					newApplication.setApplicants(userId, applicantObj);
				}
				tenantApplications.put(newApplication.getApplicationId(), newApplication);
				// save application --Arif method
			} else {
				throw new TenantException("Invalid Property Id");
			}
		} catch (DataAccessException dae) {
			throw new InvalidInputException(dae.getMessage());
		} catch (TenantException te) {
			throw new InvalidInputException(te.getMessage());
		}

	}

	// used to display application details
	private void viewApplications() {
		int i = 1;
		loadTenantInterstedProperties();
		Iterator<Map.Entry<String, RentalProperty>> iterator = tenantInterstedProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, RentalProperty> entry = iterator.next();
			rentProp = entry.getValue();
			HashMap<String, Application> rentPropApplication = rentProp.getApplications();
			Iterator<Map.Entry<String, Application>> appIterator = rentPropApplication.entrySet().iterator();
			System.out.println("-----------------------------------------------------------");
			while (appIterator.hasNext()) {
				Map.Entry<String, Application> appEntries = appIterator.next();
				Application application = appEntries.getValue();
				if (application.getApplicants().containsKey(this.getUserId())) {
					System.out.println(i + ". Property Id : " + entry.getKey());
					System.out.println("Application Id : " + appEntries.getKey());
					System.out.println("Weekly Rental Amount" + " : " + application.getWeeklyRentalAmount());
					System.out.println("Contract duration" + " : " + application.getContractduration());
					System.out.println("Application Date" + " : " + application.getApplicationDate());
					System.out.println("Number of Applicants" + " : " + application.getNoOfApplications());
					Tenant thisApplicant = application.getApplicants().get(this.getUserId());
					System.out.println("Applicants Name" + " : " + thisApplicant.getUserName());
					System.out.println("Applicants Income" + " : " + thisApplicant.getIncome());
					System.out.println("Applicants Occupation" + " : " + thisApplicant.getOccupation());
					System.out.println("Past Rental ContractId" + " : " + thisApplicant.getRentalContractRef());
					System.out.println("Length of Past Rental" + " : " + thisApplicant.getRentHistoryMonths());
					System.out.println("Past Rental ContractId" + " : " + thisApplicant.getRentalContractRef());
					System.out.println("Length of Past Rental" + " : " + thisApplicant.getRentHistoryMonths());
					System.out.println("Customer Id" + " : " + thisApplicant.getUserId());
					System.out.println("Application Status" + " : " + application.getApplicationStatus());
					System.out.println("employer Name" + " : " + thisApplicant.getEmployerName());
					System.out.println("Length of past employment" + " : " + thisApplicant.getEmploymentMonths());
				}
			}
			i++;
		}
		// application.toString();
	}

	// used to notify interested property details
	private void viewNotifications() {
		int i = 1;
		loadTenantInterstedProperties();
		Iterator<Map.Entry<String, RentalProperty>> iterator = tenantInterstedProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, RentalProperty> entry = iterator.next();
			rentProp = entry.getValue();
			System.out.println("-----------------------------------------------------------");
			System.out.println(i + " : " + entry.getKey());
			System.out.println("Property Type" + " : " + rentProp.getPropertyType());
			System.out.println("Property Status" + " : " + rentProp.getPropertyStatus());
			System.out.println("Property Address" + " : " + rentProp.getPropertyAddress());
			System.out.println("Suburb" + " : " + rentProp.getSuburb());
			System.out.println("Bathrooms" + " : " + rentProp.getNumbOfBathrooms());
			System.out.println("Bedrooms" + " : " + rentProp.getNumbOfBedrooms());
			System.out.println("Car spaces" + " : " + rentProp.getNumbOfCarSpace());
			System.out.println("Acceptable Contract Duration" + " : " + rentProp.getAcceptableContractDuration());
			System.out.println("Expected Weekly Rental Rate" + " : " + rentProp.getExpecweeklyRentalRate());
			System.out.println("Inspection Status" + " : " + rentProp.getInspectionStatus());
			i++;
		}
	}
}
