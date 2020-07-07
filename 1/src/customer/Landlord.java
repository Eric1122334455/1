package customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import common.CustomerConstants;
import common.GlobalConstants;
import dataaccess.DataAccessException;
import dataaccess.DataAccessLayer;
import property.Application;
import property.RentalProperty;
import useradministration.UserException;

public class Landlord extends Customer {

	private RentalProperty rentProp;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private HashMap<String, RentalProperty> landlordRentalProperties = new HashMap<>();

	public Landlord(String userId, String userPassword, String userName, String userEmail, char userType)
			throws UserException {
		super(userId, userPassword, userName, userEmail, userType);
	}

	// used to load the rental properties into hashmap from database
	public void loadLandLordRentalProperties() {
		try {
			DataAccessLayer dal = new DataAccessLayer();
			// landlordRentalProperties = dal.getPropertyDetails(null, null, 1, 0,
			// GlobalConstants.SALE, this.getUserId());
		} catch (DataAccessException dae) {
			System.out.println(dae.getMessage());
		}
	}

	// used to display menu
	@Override
	public boolean displayMenu() {
		try {
			System.out.println("******LANDLORD MENU*********");
			super.displayMenu();
			System.out.println(
					"View listed properties:	4\nList new properties: 5\nUpdate listed properties: 6\nSet Management Fee:	7\nView Applications: 8\nSet Application Status 9\nExit 10\nEnter your choice:");
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

			if (choice < 1 || choice > 10) {
				throw new InvalidInputException("You have made an invalid selection, please try again\r\n");
			} else if (choice >= 1 || choice <= 3) {
				super.handleMenu(choice);
			} else if (choice == 10) {
				System.out.println("You have exited the program.\r\n");
				br.close();
				System.exit(1);
			} else {
				System.out.println("====================================");
				System.out.println("\nYou have selected " + choice + "\r");
				switch (choice) {
				case 4:
					displayListedProperties();
					break;

				case 5:
					listProperty();
					displayMenu();
					break;

				case 6:
					updatePropertyDetails();
					displayMenu();
					break;

				case 7:
					setManagementFee();
					displayMenu();
					break;

				case 8:
					viewApplications();
					displayMenu();
					break;

				case 9:
					setApplicationStatus();
					displayMenu();
					break;

				case 10:
					System.exit(0);

				default:
					throw new InvalidInputException(CustomerConstants.INVALID_SELECTION + ", please try again\r\n");
				}
			}
		} catch (Exception e) {
			throw new InvalidInputException(CustomerConstants.INVALID_SELECTION);
		}
	}

	// used to display listed properties
	private void displayListedProperties() {
		int i = 1;
		loadLandLordRentalProperties();
		Iterator<Map.Entry<String, RentalProperty>> iterator = landlordRentalProperties.entrySet().iterator();
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
			System.out.println("Exepected Weekly Rental rate" + " : " + rentProp.getWeeklyRentalRate());
			System.out.println("Inspection Status" + " : " + rentProp.getInspectionStatus());
			i++;
		}
	}

	// used to list new properties
	private void listProperty() {
		System.out.println("Enter the property details");
		try {
			rentProp = new RentalProperty(captureDetails("Property Address"), captureDetails("Suburb"),
					Integer.parseInt(captureDetails("Number Of Bedrooms")),
					Integer.parseInt(captureDetails("Number Of Bathrooms")),
					Integer.parseInt(captureDetails("Number Of Carspace")), captureDetails("Property Type"),
					GlobalConstants.PROPERTY_AVAILABLE, Double.parseDouble(captureDetails("Weekly Rental Rate")),
					Double.parseDouble(captureDetails("Acceptable Contract Duration")));

			// saveproperty method
			addListProperties(rentProp);
			System.out.println("Property added sucessfully ");
		} catch (LandlordException le) {
			System.out.println(le.getMessage());
			displayMenu();
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
			listProperty();
		}
	}

	// used to add and save listed properties
	public void addListProperties(RentalProperty property) throws LandlordException {
		loadLandLordRentalProperties();
		if (property != null && !landlordRentalProperties.containsKey(property.getPropertyId())) {
			landlordRentalProperties.put(property.getPropertyId(), property);
			// save list properties
		} else {
			throw new LandlordException("Falied to add property, invlaid details provided");
		}
	}

	// returns the list of properties
	public HashMap<String, RentalProperty> getListProperties() {
		return landlordRentalProperties;
	}

	// update property details
	private void updatePropertyDetails() {
		try {
			String propertyId = captureDetails("Property Id");
			loadLandLordRentalProperties();
			rentProp = landlordRentalProperties.get(propertyId);
			if (rentProp != null) {
				System.out.println(
						"select the details to be updated\n Property Address: 1\n Suburb: 2\n Number Of Bedrooms: 3\n Number Of Bathrooms: 4\nNumber Of Carspace: 5\nAcceptable Contract Duration 6\nWeekly Rental Rate: 7\nExit: 8");
				int choice = Integer.parseInt(br.readLine());
				System.out.println("\nYou have selected " + choice + "\r");
				switch (choice) {
				case 1:
					rentProp.setPropertyAddress(captureDetails("Property Address"));
				case 2:
					rentProp.setSuburb(captureDetails("Suburb"));
				case 3:
					rentProp.setNumbOfBedrooms(Integer.parseInt(captureDetails("Number Of Bedrooms")));
				case 4:
					rentProp.setNumbOfBathrooms(Integer.parseInt(captureDetails("Number Of Bathrooms")));
				case 5:
					rentProp.setNumbOfCarSpace(Integer.parseInt(captureDetails("Number Of Carspace")));
				case 6:
					rentProp.setAcceptableContractDuration(
							Double.parseDouble(captureDetails("Acceptable Contract Duration")));
				case 7:
					rentProp.setWeeklyRentalRate(Double.parseDouble(captureDetails("Weekly Rental Rate")));
				case 8:
					displayMenu();
					break;
				default:
					throw new InvalidInputException(CustomerConstants.INVALID_SELECTION);
				}
				landlordRentalProperties.replace(propertyId, rentProp);
				// save this to database --ARIF method
			} else {
				throw new LandlordException("Invalid Id provided");
			}
		} catch (InvalidInputException iie) {
			System.out.println(iie.getMessage());
		} catch (LandlordException le) {
			System.out.println(le.getMessage());
		} catch (IOException ie) {
			System.out.println(CustomerConstants.INVALID_SELECTION);
		}
	}

	// used to negotiate management fee between landlord and employee for listed
	// property
	public void setManagementFee() throws LandlordException {
		try {
			String propertyId = captureDetails("propertyId");
			Double requestedManagementFee = Double.parseDouble(captureDetails("management fee"));
			loadLandLordRentalProperties();
			rentProp = landlordRentalProperties.get(propertyId);
			if (rentProp == null) {// check for existence of propertyid in the hashmap
				throw new LandlordException("no property listed by you with given property id");
			} else if (rentProp.getPropertyStatus().equalsIgnoreCase(GlobalConstants.PROPERTY_NOT_AVAILABLE)) {
				throw new LandlordException("Can't set Management fee on rented property");
			} else if (requestedManagementFee >= 8 && requestedManagementFee <= 6) {
				throw new LandlordException("Invalid Management fee entered");
			} else {
				if (landlordRentalProperties.size() > 1) {
					rentProp.negotiatedmanagementFeeforMultipleProperty(
							Integer.parseInt(captureDetails("discount rate")));
				} else {
					rentProp.negotiatedmanagementFeeforSingleProperty(
							Double.parseDouble(captureDetails("weekly rental rate")));
				}
			}
		} catch (InvalidInputException iie) {
			throw new LandlordException(iie.getMessage());
		}
	}

	// used to display application details
	private void viewApplications() {
		int i = 1;
		loadLandLordRentalProperties();
		Iterator<Map.Entry<String, RentalProperty>> iterator = landlordRentalProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, RentalProperty> entry = iterator.next();
			rentProp = entry.getValue();
			HashMap<String, Application> rentPropApplication = rentProp.getApplications();
			Iterator<Map.Entry<String, Application>> appIterator = rentPropApplication.entrySet().iterator();
			System.out.println("-----------------------------------------------------------");
			while (appIterator.hasNext()) {
				Map.Entry<String, Application> appEntries = appIterator.next();
				Application application = appEntries.getValue();
				System.out.println(i + ". Property Id : " + entry.getKey());
				System.out.println("Application Id" + " : " + appEntries.getValue());
				System.out.println("Weekly Rental Amount" + " : " + application.getWeeklyRentalAmount());
				System.out.println("Contract duration" + " : " + application.getContractduration());
				System.out.println("Application Date" + " : " + application.getApplicationDate());
				System.out.println("Application Status" + " : " + application.getApplicationStatus());
				HashMap<String, Tenant> rentPropApplicants = application.getApplicants();
				Iterator<Map.Entry<String, Tenant>> appDetailIterator = rentPropApplicants.entrySet().iterator();
				while (appDetailIterator.hasNext()) {
					Tenant applicant = appDetailIterator.next().getValue();
					System.out.println(">>>>>");
					System.out.println("Customer Id" + " : " + applicant.getUserId());
					System.out.println("Applicant Income" + " : " + applicant.getIncome());
					System.out.println("Past Rental Contract Id" + " : " + applicant.getRentalContractRef());
					System.out.println("lenght of past rental" + " : " + applicant.getRentHistoryMonths());
					System.out.println("Occupation" + " : " + applicant.getOccupation());
					System.out.println("employer Name" + " : " + applicant.getEmployerName());
					System.out.println("Length of past employment" + " : " + applicant.getEmploymentMonths());
				}
			}
			i++;
		}
		// application.toString();
	}

	// used to accept/ reject aplication
	private void setApplicationStatus() throws InvalidInputException {
		try {
			String appId = captureDetails("Application id");
			loadLandLordRentalProperties();
			Iterator<Map.Entry<String, RentalProperty>> iterator = landlordRentalProperties.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, RentalProperty> entry = iterator.next();
				rentProp = entry.getValue();
				HashMap<String, Application> rentPropApplication = rentProp.getApplications();
				Application application = rentPropApplication.get(appId);
				if (application != null) {
					String appDecision = captureDetails("Y to accept or N to reject application");
					if (appDecision.equalsIgnoreCase("Y")) {
						application.setApplicationStatus(GlobalConstants.APPLICATION_ACCEPTED);
						rentPropApplication.replace(appId, application);
						rentProp.setApplications(rentPropApplication);
					} else if (appDecision.equalsIgnoreCase("N")) {
						application.setApplicationStatus(GlobalConstants.APPLICATION_REJECTED);
						rentPropApplication.replace(appId, application);
						rentProp.setApplications(rentPropApplication);

					} else {
						throw new LandlordException("invalid application decision");
					}
				} else {
					throw new LandlordException("invalid application Id");
				}
			}
		} catch (LandlordException le) {
			throw new InvalidInputException(le.getMessage());
		}
	}
}