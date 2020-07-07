package customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import common.CustomerConstants;
import common.GlobalConstants;
import property.Offer;
import property.SaleProperty;
import useradministration.UserException;

public class Vendor extends Customer {

	private SaleProperty saleProp;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private HashMap<String, SaleProperty> vendorSalesProperties = new HashMap<>();

	// used to load the sale properties into hashmap from database
	public void loadVendorSalesProperties() {
		// use property search class
		// try {
		// DataAccessLayer dal = new DataAccessLayer();
		// vendorSalesProperties = dal.getPropertyDetails(null, null, 1, 0,
		// GlobalConstants.RENT, this.getUserId());
		// } catch (DataAccessException dae) {
		// System.out.println(dae.getMessage());
		// }
	}

	public Vendor(String userId, String userPassword, String userName, String userEmail, char userType)
			throws UserException {
		super(userId, userPassword, userName, userEmail, userType);
	}

	// used to display menu
	@Override
	public boolean displayMenu() {
		try {
			System.out.println("******VENDOR MENU*********");
			super.displayMenu();
			System.out.println(
					"View listed properties:	4\nList new properties: 5\nUpdate listed properties: 6\nNegotiate comission:	7\nView Offers: 8\nSet Offer Status 9\nExit 10\nEnter your choice:");
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
					negotiateCommission();
					displayMenu();
					break;

				case 8:
					viewOffers();
					displayMenu();
					break;

				case 9:
					setOfferStatus();
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

	// used to accept or reject offer
	private void setOfferStatus() throws InvalidInputException {
		try {
			String offerId = captureDetails("Offer id");
			loadVendorSalesProperties();
			Iterator<Map.Entry<String, SaleProperty>> iterator = vendorSalesProperties.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, SaleProperty> entry = iterator.next();
				saleProp = entry.getValue();
				HashMap<String, Offer> salesPropOffer = saleProp.getOffers();
				Offer offer = salesPropOffer.get(offerId);
				if (offer != null) {
					String offerDecision = captureDetails("Y to accept or N to reject offer");
					if (offerDecision.equalsIgnoreCase("Y")) {
						offer.setOfferStatus(GlobalConstants.OFFER_ACCEPTED);
						salesPropOffer.replace(offerId, offer);
						saleProp.setOffers(salesPropOffer);
					} else if (offerDecision.equalsIgnoreCase("N")) {
						offer.setOfferStatus(GlobalConstants.OFFER_REJECTED);
						salesPropOffer.replace(offerId, offer);
						saleProp.setOffers(salesPropOffer);
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

	// used to display offers
	private void viewOffers() {
		int i = 1;
		loadVendorSalesProperties();
		Iterator<Map.Entry<String, SaleProperty>> iterator = vendorSalesProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, SaleProperty> entry = iterator.next();
			saleProp = entry.getValue();
			HashMap<String, Offer> salePropOffer = saleProp.getOffers();
			Iterator<Map.Entry<String, Offer>> offerIterator = salePropOffer.entrySet().iterator();
			System.out.println("-----------------------------------------------------------");
			while (offerIterator.hasNext()) {
				Map.Entry<String, Offer> offerEntries = offerIterator.next();
				Offer offer = offerEntries.getValue();
				System.out.println(i + ". Property Id : " + entry.getKey());
				System.out.println("Offer Id" + " : " + offerEntries.getValue());
				System.out.println("Offer Amount" + " : " + offer.getOfferAmount());
				System.out.println("Customer Id" + " : " + offer.getCustomerID());
				System.out.println("Offer Date" + " : " + offer.getOfferDate());
				System.out.println("Offer Status" + " : " + offer.getOfferStatus());
			}
			i++;
		}
	}

	// used to display listed properties
	private void displayListedProperties() {
		int i = 1;
		loadVendorSalesProperties();
		Iterator<Map.Entry<String, SaleProperty>> iterator = vendorSalesProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, SaleProperty> entry = iterator.next();
			saleProp = entry.getValue();
			System.out.println("-----------------------------------------------------------");
			System.out.println(i + " : " + entry.getKey());
			System.out.println("Property Type" + " : " + saleProp.getPropertyType());
			System.out.println("Property Status" + " : " + saleProp.getPropertyStatus());
			System.out.println("Property Address" + " : " + saleProp.getPropertyAddress());
			System.out.println("Suburb" + " : " + saleProp.getSuburb());
			System.out.println("Bathrooms" + " : " + saleProp.getNumbOfBathrooms());
			System.out.println("Bedrooms" + " : " + saleProp.getNumbOfBedrooms());
			System.out.println("Car spaces" + " : " + saleProp.getNumbOfCarSpace());
			System.out.println("Sales comission" + " : " + saleProp.getSalesCommision());
			System.out.println("Minimum Price" + " : " + saleProp.getMinimumPrice());
			System.out.println("Inspection Status" + " : " + saleProp.getInspectionStatus());
			i++;
		}
	}

	// used to list new properties
	private void listProperty() {
		System.out.println("Enter the property details");
		try {
			saleProp = new SaleProperty(captureDetails("Property Address"), captureDetails("Suburb"),
					Integer.parseInt(captureDetails("Number Of Bedrooms")),
					Integer.parseInt(captureDetails("Number Of Bathrooms")),
					Integer.parseInt(captureDetails("Number Of Carspace")),
					Double.parseDouble(captureDetails("Minimum Price")),
					Double.parseDouble(captureDetails("Sales Commission")), captureDetails("Property Type"),
					GlobalConstants.PROPERTY_AVAILABLE);
			// save property method
			addListProperties(saleProp);
			System.out.println("Property added sucessfully ");
		} catch (VendorException ve) {
			System.out.println(ve.getMessage());
			displayMenu();
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
			listProperty();
		}
	}

	// used to add and save listed properties
	public void addListProperties(SaleProperty property) throws VendorException {
		loadVendorSalesProperties();
		if (property != null && !vendorSalesProperties.containsKey(property.getPropertyId())) {
			vendorSalesProperties.put(property.getPropertyId(), property);
			// save list properties-- ARIF method
		} else {
			throw new VendorException("Falied to add property, invlaid details provided");
		}
	}

	// update property details
	private void updatePropertyDetails() {
		try {
			String propertyId = captureDetails("Property Id");
			loadVendorSalesProperties();
			saleProp = vendorSalesProperties.get(propertyId);
			if (saleProp != null) {
				System.out.println(
						"select the details to be updated\n Property Address: 1\n Suburb: 2\n Number Of Bedrooms: 3\n Number Of Bathrooms: 4\nNumber Of Carspace: 5\nSales commission : 6\nMinimum Price: 7\nExit: 8");
				int choice = Integer.parseInt(br.readLine());
				System.out.println("\nYou have selected " + choice + "\r");
				switch (choice) {
				case 1:
					saleProp.setPropertyAddress(captureDetails("Property Address"));
				case 2:
					saleProp.setSuburb(captureDetails("Suburb"));
				case 3:
					saleProp.setNumbOfBedrooms(Integer.parseInt(captureDetails("Number Of Bedrooms")));
				case 4:
					saleProp.setNumbOfBathrooms(Integer.parseInt(captureDetails("Number Of Bathrooms")));
				case 5:
					saleProp.setNumbOfCarSpace(Integer.parseInt(captureDetails("Number Of Carspace")));
				case 6:
					saleProp.setSalesCommision(Double.parseDouble(captureDetails("Sales Commission")));
				case 7:
					saleProp.setMinimumPrice(Double.parseDouble(captureDetails("Minimum Price")));
				case 8:
					displayMenu();
					break;
				default:
					throw new InvalidInputException(CustomerConstants.INVALID_SELECTION);
				}
				vendorSalesProperties.replace(propertyId, saleProp);
				// save this to database --arif method
			} else {
				throw new VendorException("Invalid Id provided");
			}
		} catch (InvalidInputException iie) {
			System.out.println(iie.getMessage());
		} catch (VendorException le) {
			System.out.println(le.getMessage());
		} catch (IOException ie) {
			System.out.println(CustomerConstants.INVALID_SELECTION);
		}
	}

	// used to negotiate commission
	private void negotiateCommission() throws VendorException {
		try {
			String propertyId = captureDetails("propertyId");
			Double requestedCommission = Double.parseDouble(captureDetails("commission to be requested"));
			loadVendorSalesProperties();
			saleProp = vendorSalesProperties.get(propertyId);
			if (saleProp == null) {// check for existence of propertyid in the hashmap
				throw new VendorException("no property listed by you with given property id");
			} else if (saleProp.getPropertyStatus().equalsIgnoreCase(GlobalConstants.PROPERTY_NOT_AVAILABLE)) {
				throw new VendorException("Can't set commission fee sold property");
			} else if (requestedCommission >= 2 && requestedCommission <= 5) {
				throw new VendorException("Invalid commission rate entered");
			} else {
				saleProp.setSalesCommision(requestedCommission);
			}
		} catch (InvalidInputException iie) {
			throw new VendorException(iie.getMessage());
		}
	}

}
