package dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import common.GlobalConstants;
import property.Property;

public class PropertySearch implements GlobalConstants{
	
	private HashMap<String, Property> propertyList = new HashMap<String, Property>();
	private Scanner console = new Scanner(System.in);
	private char type; // SALE or RENTAL
	private String propertyId;

	public PropertySearch(String id) {
		this.propertyId = id;
		this.getPropertyfromId(propertyId);
	}

	
	public PropertySearch(char type) {
		this.type = type;
		this.showPropertySearchOptions();
	}

	
	public PropertySearch() {
		this('X'); // X indicates that search is for both properties to buy and lease
	}

	
	// get search results
	public HashMap<String, Property> getSearchResults() {
		return this.propertyList;
	}


	// display the property search options
	private void showPropertySearchOptions() {
		
		boolean returnVal = true;

		String propertyId = "";
		String suburb = "";
		
		int minPrice = 0;
		int maxPrice = 0;
		
		while (returnVal) {
			System.out.println("Enter search option:");
			System.out.println(String.format("%-30s", "1. Property Id"));
			System.out.println(String.format("%-30s", "2. Suburb"));
			System.out.println(String.format("%-30s", "3. Price Range"));
			System.out.println(String.format("%-30s", "4. Suburb and Price Range"));
			System.out.println(String.format("%-30s", "5. Return to previous menu"));
			
			int option = Integer.parseInt(console.nextLine());
		
			switch (option) {
			
				case 1: 
					System.out.println("Enter property id:");
					propertyId = console.nextLine();
					
					this.getPropertyfromId(propertyId);					
					break;
					
				case 2:
					System.out.println("Enter suburb:");
					suburb = console.nextLine();
					
					this.getPropertyfromSuburb(suburb);					
					break;

				case 3:
					System.out.println("Enter minimum price:");
					minPrice = Integer.parseInt(console.nextLine());

					System.out.println("Enter maximum price:");
					maxPrice = Integer.parseInt(console.nextLine());

					this.getPropertyfromPrice(minPrice, maxPrice);					
					break;

				case 4:
					System.out.println("Enter suburb:");
					suburb = console.nextLine();
					
					System.out.println("Enter minimum price:");
					minPrice = Integer.parseInt(console.nextLine());

					System.out.println("Enter maximum price:");
					maxPrice = Integer.parseInt(console.nextLine());

					this.getPropertyfromSuburbPrice(suburb, minPrice, maxPrice);
					break;
					
				case 5:
					returnVal = false;
					break;
				
				default:
					break;
			}
		}
	}
	
	
	private void getPropertyfromSuburbPrice(String suburb, int minPrice, int maxPrice) {
		this.getPropertyDetails("", suburb, minPrice, maxPrice);
	}


	private void getPropertyfromPrice(int minPrice, int maxPrice) {
		this.getPropertyDetails("", "", minPrice, maxPrice);
	}


	private void getPropertyfromSuburb(String suburb) {
		this.getPropertyDetails("", suburb, 0, 0);
	}


	private void getPropertyfromId(String propertyId) {
		this.getPropertyDetails(propertyId, "", 0, 0);
	}


	private void getPropertyDetails(String id, String suburb, int priceFrom, int priceTo) {
		try {

			DataAccessLayer dal = new DataAccessLayer();

			this.propertyList = dal.getPropertyDetails(id, suburb,priceFrom, priceTo, this.type, "");
			this.showSearchResults();

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public HashMap<String, Property> getPropertyForCustomer(String userId) {
		try {
			DataAccessLayer dal = new DataAccessLayer();

			this.propertyList = dal.getPropertyDetails("", "", 0, 0, ' ', userId);
			this.showSearchResults();

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		
		return this.getSearchResults();
	}
	
	
	// show search results
	private void showSearchResults() {
		
		for (Map.Entry<String, Property> entry : this.getSearchResults().entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}
}