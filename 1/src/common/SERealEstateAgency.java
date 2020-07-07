package common;

import useradministration.User;
import useradministration.UserAccessControl;
import useradministration.UserException;

import dataaccess.DataAccessException;
import dataaccess.DataAccessLayer;
import dataaccess.PropertySearch;

import java.util.Scanner;

public class SERealEstateAgency implements GlobalConstants{
	
	// initialise scanner object
    private Scanner	console = new Scanner(System.in);
    
	// current user
	public User		currentUser;
	
	// constructor
	public SERealEstateAgency() {

	}
	

	// run application 
	public void runApp() throws GeneralException {

		// variable used to capture selected menu option
	    String	option = "";
	    
	    // variable used to abort/re-display menu
	    boolean returnVal = true;
	    
	    // display the menu
	    do {
	    	try {

	    		if (ADMINMODE) {
	    			this.showAdminMenu();
	    		} else {
		    		// show menu
		        	this.showBasicMenu();
	    		}
	    		
	        	// get option
	        	option = console.nextLine();
	        	
	        	// check option entered is valid
	        	this.isValidMenuOption(option);
	        		
	       		// action the selected option
	        	if (ADMINMODE) {
	        		returnVal = this.actionAdminMenu(Integer.parseInt(option));
	        	} else {
	        		returnVal = this.actionBasicMenu(Integer.parseInt(option));
	        	}
	    	}

	    	// catch any program exceptions
	    	catch (GeneralException e) {
	
	        	// error occurred; show menu again
	        	System.out.println(e.getMessage());
	       	
	        }
	
	    	// catch any runtime exceptions
	    	// this will terminate program
	        catch (Exception e) {
	
	        	// runtime error occurred; terminate program
	        	System.out.println("Unexpected error occurred. " + e.getMessage() + ". Program terminated.");
	       	
	        	returnVal = false;
	        }
	
	    } while (returnVal == true);
	}
	
	public void showAdminMenu() {
		// System is being run in admin mode
		// creation of schema is required
		System.out.println("**** S&E Real Estate Agency MAIN MENU ****\n"
							+ "Create schema:	1\n"
							+ "Exit:		2\n");
	}
	

	// show menu
	public void showBasicMenu() {

		System.out.println("**** S&E Real Estate Agency MAIN MENU ****\n"
							+ "Search Properties:	1\n"
							+ "Login:			2\n" 
							+ "Register:		3\n"
							+ "Exit:			4\n" 
							+ "Enter your choice:");
	}

	
	// validate the selected option
	public void isValidMenuOption(String option) throws GeneralException{
		
    	try {
    		this.isValidMenuOption(Integer.parseInt(option));
    	}
    	
    	catch (NumberFormatException nse){
    		throw new GeneralException("Selected Option not available. Must be an number between 1 and 4.");
    	}
    }

	
    // validate the selected option; must be an integer 1-7
    public void isValidMenuOption(int option) throws NumberFormatException{

    	// get max option depending on mode system is run
    	int i = (ADMINMODE) ? 2 : 4;
    	
    	if (option < 1 || option > i) {
    		throw new NumberFormatException("Selected Option not available. Must be an number between 1 and 7.");
    	}
    }
    

	// action selected menu option
    public boolean actionAdminMenu(int option) throws GeneralException{

        boolean	returnVal = true;
        
        try {
            
	        switch (option) {
	
	        	// create schema & exit system immediately
	        	case 1:	
	        		DataAccessLayer dal = new DataAccessLayer();
					dal.createSchema();
					dal.addData();
				
				// Exit
				case 2:
					returnVal = this.actionBasicMenu(4);
					break;
	        }
        }
        
        catch (DataAccessException dae) {
        	throw new GeneralException (dae.getMessage());
        }
        
        catch (Exception e) {
        	throw new GeneralException (e.getMessage());
        }       

        return returnVal;
    }
    
	// action selected menu option
    public boolean actionBasicMenu(int option) throws GeneralException{

        boolean	returnval = true;
        
        UserAccessControl uac = new UserAccessControl();
        
         try {
        
	        switch (option) {
	
				case 1:	

					PropertySearch ps = new PropertySearch();
					break;
				
				// Login User (can be customer or employee)
				case 2:
					
					try {
						this.setCurrentUser(uac.loginUser());
						returnval = this.displayUserMenu();
					}
					
					catch (UserException uae) {
						System.out.println(uae.getMessage());
						throw new GeneralException (uae.getMessage());
					}
					
					break;
	
				// Register User (customer)
				case 3:

					try {
						this.setCurrentUser(uac.registerUser());
						returnval = this.displayUserMenu();
					}
					
					catch (UserException uae) {
						System.out.println(uae.getMessage());
						throw new GeneralException (uae.getMessage());
					}

					break;
					
				// Exit Program
				case 4:returnval = false;
					break;
							
				// any other input; do nothing, return true to display the menu again
				default:
					break;
	        }
        }
        
        catch (Exception e) {
        	System.out.println(e.getMessage());
        	returnval = false;
        }

        if (!returnval) {
			System.out.println("*** End ***");
        }
        
        return returnval;
	}
    
    
    public boolean displayUserMenu() {
    	boolean returnVal = true;
    	
    	while(returnVal) {
    		returnVal = this.getCurrentUser().displayMenu();
    	}
    	
    	return returnVal;
    }

   
    public User getCurrentUser() {
    	return this.currentUser;
    }
    

    public void setCurrentUser(User user) {
    	this.currentUser = user;
    }
 }