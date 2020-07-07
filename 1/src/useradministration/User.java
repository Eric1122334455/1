package useradministration;
import common.GlobalConstants;
import dataaccess.DataAccessException;
import dataaccess.DataAccessLayer;

public abstract class User implements GlobalConstants{

	private String	userId;
	private String	userPassword;
	private	String	userName;
	private String	userEmail;
	
	private char	userGroup = PUBLIC;	// by default user is PUBLIC
	

	public User(String userId, String userPassword, String userName, 
									String userEmail ) {

		this.userId			= userId;
		this.userPassword	= userPassword;
		this.userName		= userName;
		this.userEmail		= userEmail;
	}

	
	public String getUserId() {
		return this.userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return this.userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserPassword() {
		return this.userPassword;
	}


	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}


	public String getUserEmail() {
		return this.userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public char getUserGroup() {
		return this.userGroup;
	}


	public void setUserGroup(char userGroup) {
		this.userGroup = userGroup;
	}
	
	
	// save user details
	public void saveUser() throws UserException, ClassNotFoundException {
		try {
			
			// call data access layer
			DataAccessLayer dal = new DataAccessLayer();
			dal.saveUser(this);
		}
		
		catch (DataAccessException dae) {
			throw new UserException("Failed to save user.");
		}
	}

	public abstract boolean displayMenu();
}
