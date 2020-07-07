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
import property.DateTime;
import property.Offer;
import property.SaleProperty;
import useradministration.UserException;

public class Buyer extends Customer {

	public Buyer(String userId, String userPassword, String userName, String userEmail, char userType)
			throws UserException {
		super(userId, userPassword, userName, userEmail, userType);
	}

	private SaleProperty saleProp;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private HashMap<String, SaleProperty> buyerInterstedProperties = new HashMap<>();
	private HashMap<String, Offer> buyerOffers = new HashMap<>();

	// used to load the interested properties into hashmap from database
	public void loadBuyerInterestedProperties() {
		// buyerInterstedProperties = PropertySearch.
	}

	// used to update the interested properties into database from hashmap
	public void updateBuyerInterestedProperties() {
		// buyerInterstedProperties = PropertySearch.
	}

	// used to load the buyer offers into hashmap from database
	public void loadBuyerOffers() {
		DataAccessLayer dal;
		try {
			dal = new DataAccessLayer();
			buyerOffers = dal.getOffersCustomer(this.getUserId());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
	}

	// used to display menu
	@Override
	public boolean displayMenu() {
		try {
			System.out.println("******BUYER MENU*********");
			super.displayMenu();
			System.out.println(
					"View Notifications: 4\nView Offers: 5\nCreate Offer: 6\nWithdraw offer:	7\nMake down payment: 8\nAdd interested properties 9\nRemove interested properties 10\nExit 11\nEnter your choice:");
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
					viewOffers();
					displayMenu();
					break;

				case 6:
					createOffer();
					displayMenu();
					break;

				case 7:
					withdrawOffer();
					displayMenu();
					break;

				case 8:
					makeDownPayment();
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
			if (ps.getSearchResults().containsKey(propertyId) && buyerInterstedProperties.containsKey(propertyId)) {
				buyerInterstedProperties.remove(propertyId);
				// save buyer interested properties
				updateBuyerInterestedProperties();
			} else {
				throw new BuyerException("invalid property id provided");
			}
		} catch (BuyerException be) {
			throw new InvalidInputException(be.getMessage());
		}
	}

	// used to add interesting properties
	private void addInterestedProperties() throws InvalidInputException {
		try {
			String propertyId = captureDetails("property Id");
			PropertySearch ps = new PropertySearch(propertyId);
			if (ps.getSearchResults().containsKey(propertyId)) {
				SaleProperty prop = (SaleProperty) ps.getSearchResults().get(propertyId);
				buyerInterstedProperties.put(propertyId, prop);
				// save buyer interested properties
				updateBuyerInterestedProperties();
			} else {
				throw new BuyerException("invalid property id provided");
			}
		} catch (BuyerException be) {
			throw new InvalidInputException(be.getMessage());
		}
	}

	// used to make payment for an offer
	private void makeDownPayment() throws InvalidInputException {
		try {
			DataAccessLayer dal = new DataAccessLayer();
			String propertyId = captureDetails("Property Id");
			loadBuyerInterestedProperties();
			if (buyerInterstedProperties.containsKey(propertyId)) {
				String offerId = captureDetails("Offer Id");
				loadBuyerOffers();
				Offer thisOffer = buyerOffers.get(offerId);
				if (thisOffer != null && thisOffer.getOfferStatus() == GlobalConstants.OFFER_ON_HOLD) {
					double offeredPrice = Double.parseDouble(captureDetails("price offered"));
					DateTime date = new DateTime();
					saleProp.acceptSalesOffer(offeredPrice, date);
					dal.saveUser(this);
				} else {
					throw new BuyerException("no property found with given id");
				}
			} else {
				throw new BuyerException("no offfer found with given id");
			}
		} catch (BuyerException be) {
			throw new InvalidInputException(be.getMessage());
		} catch (DataAccessException de) {
			throw new InvalidInputException(de.getMessage());
		}
	}

	// used to withdraw an offer
	private void withdrawOffer() throws InvalidInputException {
		String offerId = captureDetails("Offer Id");
		loadBuyerOffers();
		Offer thisOffer = buyerOffers.get(offerId);
		if (thisOffer != null) {
			thisOffer.setOfferStatus(GlobalConstants.OFFER_WITHDRAWN);
		} else {
			throw new InvalidInputException("no offer found with given id");
		}
	}

	// used to create a new offer
	private void createOffer() throws InvalidInputException {
		try {
			String salePropId = captureDetails("property id");
			HashMap<String, SaleProperty> listedSaleProp = saleProp.getListedSaleProperties();
			SaleProperty saleProperty = listedSaleProp.get(salePropId);
			if (saleProperty != null) {
				HashMap<String, Offer> salePropOffer = saleProp.getOffers();
				Iterator<Map.Entry<String, Offer>> offerIterator = salePropOffer.entrySet().iterator();
				while (offerIterator.hasNext()) {
					Map.Entry<String, Offer> offerEntries = offerIterator.next();
					Offer offer = offerEntries.getValue();
					if (offer.getCustomerID().equalsIgnoreCase(this.getUserId())) {
						throw new BuyerException(
								"an offer already exists with this property, can't create a new offer");
					}
				}
				DateTime now = new DateTime();
				Offer newOffer = new Offer(this.getUserId(), now, Double.parseDouble(captureDetails("Offer amount")),
						GlobalConstants.OFFER_ON_HOLD);
				buyerOffers.put(newOffer.getOfferID(), newOffer);
				// save this offer --Arif method
			} else {
				throw new BuyerException("Invalid Property Id");
			}
		} catch (BuyerException be) {
			throw new InvalidInputException(be.getMessage());
		}

	}

	// used to display offers
	private void viewOffers() {
		int i = 1;
		loadBuyerInterestedProperties();
		Iterator<Map.Entry<String, SaleProperty>> iterator = buyerInterstedProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, SaleProperty> entry = iterator.next();
			saleProp = entry.getValue();
			HashMap<String, Offer> salePropOffer = saleProp.getOffers();
			Iterator<Map.Entry<String, Offer>> offerIterator = salePropOffer.entrySet().iterator();
			System.out.println("-----------------------------------------------------------");
			while (offerIterator.hasNext()) {
				Map.Entry<String, Offer> offerEntries = offerIterator.next();
				Offer offer = offerEntries.getValue();
				if (offer.getCustomerID().equalsIgnoreCase(this.getUserId())) {
					System.out.println(i + ". Property Id : " + entry.getKey());
					System.out.println("Offer Id : " + offerEntries.getValue());
					System.out.println("Offer Amount" + " : " + offer.getOfferAmount());
					System.out.println("Offer Status" + " : " + offer.getOfferStatus());
					System.out.println("Offer Date" + " : " + offer.getOfferDate());
					System.out.println("Customer Id" + " : " + offer.getCustomerID());
				}
			}
			i++;
		}
	}

	// used to handle menu
	public boolean handleMenu(int choice) throws InvalidInputException {
		try {
			System.out.println("====================================");
			System.out.println("\nYou have selected " + choice + "\r");
			switch (choice) {
			case 1:
				System.out.println("Search properties");
				break;

			case 2:
				System.out.println("View Profile");
				break;

			case 3:
				System.out.println("Update Profile");
				break;

			default:
				throw new InvalidInputException(CustomerConstants.INVALID_SELECTION + ", please try again\r\n");
			}
		} catch (Exception e) {
			throw new InvalidInputException("You have made an invalid selection");
		}
		return false;
	}

	// used to notify interested property details
	private void viewNotifications() {
		int i = 1;
		loadBuyerInterestedProperties();
		Iterator<Map.Entry<String, SaleProperty>> iterator = buyerInterstedProperties.entrySet().iterator();
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
			System.out.println("Minimum price" + " : " + saleProp.getMinimumPrice());
			System.out.println("Inspection Status" + " : " + saleProp.getInspectionStatus());
			i++;
		}
	}
}
