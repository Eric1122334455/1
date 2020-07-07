package dataaccess;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

import common.GlobalConstants;
import customer.Buyer;
import customer.Customer;
import customer.Landlord;
import customer.Tenant;
import customer.Vendor;
import employee.Employee;
import property.Application;
import property.DateTime;
import property.Offer;
import property.Property;
import property.RentalProperty;
import property.SaleProperty;
import useradministration.User;
import useradministration.UserException;

public class DataAccessLayer implements GlobalConstants  {

	public DataAccessLayer() throws DataAccessException{

	}


	// generate a new Id
	public String genNewId(String tableName) 
			throws DataAccessException {
		
		String returnVal = "";
		String sqlStr = "";
		
		if (tableName.isEmpty()) {
			throw new DataAccessException("No table specified.");
		}

		try {

			sqlStr = "SELECT COUNT(1) AS TOTAL_RECORDS FROM " + tableName ;
			ResultSet r = executeSQL(sqlStr, 'Q');

			// move pointer to last record
			r.next();
			
			int nextId = Integer.parseInt(r.getString(1)) + 1;
			
			// get last id, add 1 and pad with trailing 0's as required
			returnVal = String.format("%05d", nextId);

		} catch (SQLException | DataAccessException e) {
			throw new DataAccessException("Table not found.");
		} 

		return returnVal;
	}
	

	// verify the user login
	public boolean verifyLogin(String userId, String password)  
			throws DataAccessException {

		boolean returnVal = false;

		if (userId.isEmpty()) {
			throw new DataAccessException("User Id is required.");
		}

		if (password.isEmpty()) {
			throw new DataAccessException("Password is required.");
		}

		if (!checkUserExists(userId)) {
			throw new DataAccessException("User not held on file.");
		}
		
		try {
			
			ResultSet r = executeSQL("SELECT userid FROM user WHERE userid = '" + userId + "' AND password = '" + password + "'", 'Q');
			
			// moves pointer to first row; return exception when no rows are selected
			while (r.next()) {
				returnVal = true;
			}

		} catch (SQLException | DataAccessException e) {
			throw new DataAccessException ("Login failed.");
		}
		
		return returnVal;
	}
	
	// save Application
	public boolean saveApplication(Application a) 
			throws DataAccessException {
		
		try {
			
			String		applicationid		= a.getApplicationId();
			String		propertyid			= a.getPropertyId();
			double		weeklyrentalamount	= a.getWeeklyRentalAmount();
			int			contractduration	= a.getContractduration();
			String		applicationstatus	= a.getApplicationStatus();
			int			numberofapplicants	= a.getNoOfApplications();
			DateTime	applicationDate		= a.getApplicationDate();
			
			// create list of applicants
			HashMap<String, Tenant> applicants = a.getApplicants();
			
			// check if the application already exists
			boolean applicationexists = checkApplicationExists(applicationid);
			
			if (!applicationexists) {
				
				applicationid = this.genNewId("application");
				
				this.executeSQL("INSERT INTO application"
								+ " ( applicationid, propertyid, weeklyrentalamount, contractduration, "
								+ "		applicationstatus, numberofapplicants, applicationDate )"
								+ " VALUES "
								+ " ( '" + applicationid + "', '" + propertyid + "', " + weeklyrentalamount + ", " 
											+ contractduration + ", '" +  applicationstatus + "', " 
											+ numberofapplicants + ", " + applicationDate + " )"
								, 'I');
				
			} else {
				
				this.executeSQL("UPDATE application"
									+ " SET propertyid = '" + propertyid + "'"
									+ ", weeklyrentalamount = " + weeklyrentalamount 
									+ ", contractduration = " + contractduration 
									+ ", applicationstatus = '" + applicationstatus + "'"
									+ ", numberofapplicants = " + numberofapplicants 
									+ ", applicationDate = " + applicationDate 
									, 'U');
			}

			// add applicants
			for (Entry<String, Tenant> tenant : applicants.entrySet()) {
				
				String customerid = tenant.getValue().getCustomerId();
				
				// check if the applicant exists for the application
				if (!checkApplicantExists(applicationid, customerid)) {
				
					this.executeSQL("INSERT INTO applicationcustomers"
							+ " ( applicationid, customerid )"
							+ " VALUES "
							+ " ( '" + applicationid + "', '" + customerid + "' )"
							, 'I');
				}			
			}
					
		} catch (DataAccessException | SQLException e) {
			throw new DataAccessException ("Failed to update details. " + e.getMessage());
		} 
		
		return true;
	}
	
	
		private boolean checkApplicationExists(String applicationId) {

		boolean returnVal = false;
		
		if (!applicationId.isEmpty()) {

			try {
				
				ResultSet r = executeSQL("SELECT applicationId FROM application WHERE applicationId = '" + applicationId + "'", 'Q');
				
				while (r.next()) {
					returnVal = true;
				}
	
			} catch (DataAccessException | SQLException e) {
				
				System.out.println(e.getMessage());
				returnVal = false;
			}
		}
		
		return returnVal;
	}

	
	private boolean checkApplicantExists(String applicationId, String customerId) {

		boolean returnVal = false;
		
		if (!applicationId.isEmpty() && !customerId.isEmpty()) {

			try {
				
				ResultSet r = executeSQL("SELECT applicationid FROM applicationcustomers "
											+ "WHERE applicationid = '" + applicationId + "'"
											+ " and customerid = '" + customerId + "'", 'Q');
				
				while (r.next()) {
					returnVal = true;
				}
	
			} catch (DataAccessException | SQLException e) {
				
				System.out.println(e.getMessage());
				returnVal = false;
			}
		}
		
		return returnVal;
	}
	
	
	// save Offer
	public boolean saveOffer(Offer o) 
			throws DataAccessException {
		try {
			
			String		offerid		= o.getOfferID();
			String		propertyid	= o.getPropertyId();
			String		customerid	= o.getCustomerID();
			Double		offerAmount	= o.getOfferAmount();
			DateTime	offerDate	= o.getOfferDate();
			char		offerStatus	= o.getOfferStatus();
			
			boolean offerexists = checkOfferExists(offerid);
			
			if (!offerexists) {
				
				offerid = this.genNewId("offer");
				
				this.executeSQL("INSERT INTO offer"
								+ " ( offerid, propertyid, customerid, offerAmount, "
								+ "		offerDate, offerStatus )"
								+ " VALUES "
								+ " ( '" + offerid + "', '" + propertyid + "', '" + customerid + "', " 
											+ offerAmount + ", '" +  offerDate + "', '"+ offerStatus + "')"
								, 'I');
			} else {
				
				this.executeSQL("UPDATE offer"
									+ " SET propertyid	= '" + propertyid	+ "'"
									+ ", customerid		= '" + customerid	+ "'"
									+ ", offerAmount	= "  + offerAmount 
									+ ", offerDate		= '" + offerDate	+ "'"
									+ ", offerStatus	= '" + offerStatus	+ "'"
									, 'U');
			}
			
		} catch (DataAccessException | SQLException e) {
			throw new DataAccessException ("Failed to update details. " + e.getMessage());
		} 
		
		return true;
	}
	

	private boolean checkOfferExists(String offerId) {
		
		boolean returnVal = false;
		
		if (!offerId.isEmpty()) {

			try {
				
				ResultSet r = executeSQL("SELECT offerId FROM offer WHERE offerId = '" + offerId + "'", 'Q');
				
				while (r.next()) {
					returnVal = true;
				}
	
			} catch (DataAccessException | SQLException e) {
				
				System.out.println(e.getMessage());
				returnVal = false;
			}
		}
		
		return returnVal;
	}
	
	
	// save Property
	public boolean saveProperty(Property p) 
			throws DataAccessException {

		try {
			
			String	propertyid	= p.getPropertyId();
			String	address		= p.getPropertyAddress();
			String	suburb		= p.getSuburb();
			int		bedroom		= p.getNumbOfBedrooms();
			int		carspace	= p.getNumbOfBathrooms();
			char	type		= p.getPropertyType().charAt(0);
			String	status		= p.getPropertyStatus();

			double	managementfee	= 0.00;
			double	weeklyrentalfee	= 0.00;
			int		rentduration	= 0;

			double	minimumprice	= 0.00;
			double	customerprice	= 0.00;
			double	commission		= 0.00;
			
			switch (type) {
				case RENTAL:
					managementfee	= ((RentalProperty)p).getManaagementFee();
					weeklyrentalfee	= ((RentalProperty)p).getWeeklyRentalRate();
					rentduration	= (int)((RentalProperty)p).getAcceptableContractDuration();
					break;
				case SALE:
					minimumprice	= ((SaleProperty)p).getMinimumPrice();
					customerprice	= ((SaleProperty)p).getMinimumPrice();
					commission		= ((SaleProperty)p).getSalesCommision();
					break;
			}
		
			boolean	propertyExists	= checkPropertyExists(propertyid);;
		
			if (!propertyExists) {
				// insert a new property
				propertyid = this.genNewId("property");

				this.executeSQL("INSERT INTO property"
									+ " ( propertyid, address, suburb, bedroom, carspace, type, status )"
									+ " VALUES "
									+ " ( '" + propertyid + "', '" + address + "' )"
									, 'I');
				
				switch (type) {
					case RENTAL:
						this.executeSQL("INSERT INTO rentalproperties"
											+ " ( propertyid, managementfee, weeklyrentalfee, rentduration )"
											+ " VALUES "
											+ " ( '" + propertyid + "', " + managementfee + ", " + weeklyrentalfee + ", " + rentduration + ")"
											, 'I');
						
					case SALE:
						this.executeSQL("INSERT INTO saleproperties"
											+ " ( propertyid, minimumprice, customerprice, commission )"
											+ " VALUES "
											+ " ( '" + propertyid + "', " + minimumprice + ", " + customerprice + ", " + commission + ")"
											, 'I');						
				}
				
			} else {
				
				this.executeSQL("UPDATE property "
								+ "SET address = '" + address + "' "
								+ " ,  suburb  = '" + suburb + "' "
								+ " ,  bedroom = "  + bedroom + " "
								+ " ,  carspace= "  + carspace + " "
								+ " ,  type    = '" + type + "' "
								+ " ,  status  = '" + status + "' "
								+ "WHERE propertyid = '" + propertyid + "'"
								, 'U');
				
				switch (type) {
					case RENTAL:
						this.executeSQL("UPDATE rentalproperties"
											+ "SET managementfee  = " + managementfee
											+ " ,  weeklyrentalfee= " + weeklyrentalfee
											+ " ,  rentduration   = " + rentduration
											+ " WHERE propertyid  ='" + propertyid + "'"
											, 'U');
						break;
					
					case SALE:
						this.executeSQL("UPDATE saleproperties"
											+ "SET minimumprice = " + minimumprice
											+ " ,  customerprice= " + weeklyrentalfee
											+ " ,  commission   = " + commission
											+ " WHERE propertyid='" + propertyid + "'"
											, 'U');
						break;
				}
			}
		
	} catch (DataAccessException | SQLException e) {
		throw new DataAccessException ("Failed to update details. " + e.getMessage());
	} 
		
		return true;
	}
	
	
	private boolean checkPropertyExists(String propertyid) {

		boolean returnVal = false;
		
		if (!propertyid.isEmpty()) {

			try {
				
				ResultSet r = executeSQL("SELECT propertyid FROM property WHERE propertyid = '" + propertyid + "'", 'Q');
				
				while (r.next()) {
					returnVal = true;
				}
	
			} catch (DataAccessException | SQLException e) {
				
				System.out.println(e.getMessage());
				returnVal = false;
			}
		}
		
		return returnVal;
	}


	// save User Credentials
	public boolean saveUser(User u) 
			throws DataAccessException {
		
		String	userId		= u.getUserId();
		String	password	= u.getUserPassword();
		String	name		= u.getUserName();
		String	eMail		= u.getUserEmail();
		char	userGroup	= u.getUserGroup();
		
		if(userId.isEmpty() || password.isEmpty() || name.isEmpty() || eMail.isEmpty() || 
				( userGroup != CUSTOMER && userGroup != EMPLOYEE )) {
			throw new DataAccessException ("User not saved. Missing mandatory data.");
		}
		
		try {
			boolean	userExists	= checkUserExists(userId);
	
			// update user
			if (userExists) {
				
				// update user
				this.executeSQL("UPDATE user"
								+ " SET password = '" + password + "'"
								+ " FROM user"
								+ " WHERE user.userid = '" + userId + "'", 'U');
			} else {
				
				// insert user
				this.executeSQL("INSERT INTO user"
								+ " ( userid, password )"
								+ " VALUES "
								+ " ( '" + userId + "', '" + password + "' )", 'I');
			}
			
			if (userGroup == CUSTOMER) {
				
				// get Customer specific details
				char	userType	= ((Customer)u).getUserType();	
				double	income		= ((Customer)u).getIncome();
				String	occupation	= ((Customer)u).getOccupation();
				int		rentHistoryMonths	= ((Customer)u).getRentHistoryMonths();
				String	rentalContractRef	= ((Customer)u).getRentalContractRef(); 
				String	employerName		= ((Customer)u).getEmployerName();  
				int		employmentMonths	= ((Customer)u).getEmploymentMonths(); 
				
				if (userExists) {
					
					// update customer
					this.executeSQL("UPDATE customer"
									+ " SET userid       = '"	+ userId	+ "'"
									+ "	  , name         = '"	+ name		+ "'"
									+ "   , emailAddress = '"	+ eMail		+ "'"
									+ "   , type         = '"	+ userType	+ "'"
									+ "   , income       = '"	+ Double.toString(income) + "'"
									+ "   , occupation   = '"	+ occupation+ "'"
									+ "   , rentHistoryMonths   = '"	+ rentHistoryMonths+ "'"
									+ "   , rentalContractRef   = '"	+ rentalContractRef+ "'"
									+ "   , employerName   		= '"	+ employerName+ "'"
									+ "   , employmentMonths	= '"	+ employmentMonths+ "'"
									+ " WHERE customer.userid = '" + userId + "'", 'U');
	
				} else {
	
					// generate a new customerid
					String customerId = genNewId("customer");
						
					// add insert statement
					this.executeSQL("INSERT INTO customer"
									+ " ( customerid, userid, name, emailAddress, type, income, occupation,"
									+ " rentHistoryMonths, rentalContractRef, employerName, employmentMonths)"
									+ " VALUES "
									+ " ( '" + customerId + "', '" + userId  + "', '" + name  + "', '" + eMail + "', '" 
											+ userType + "', " + Double.toString(income) + ", '" + occupation + "', "
											+ rentHistoryMonths + ", '" + rentalContractRef + "', '"
											+ employerName  + "', " +  employmentMonths + ")", 'I');
				}
	
			} else if (userGroup == EMPLOYEE) {
				
				char	empRole		= ((Employee)u).getEmployeeRole();
				char	partTime	= ((Employee)u).getPartTime();
	
				if (userExists) {
					
					// update employee
					this.executeSQL("UPDATE employee"
									+ " SET userid   = '"	+ userId	+ "'"
									+ "	  , name     = '"	+ name		+ "'"
									+ "   , parttime = '"	+ partTime	+ "'"
									+ " WHERE employee.userid = '" + userId + "'", 'U');
					
					// update employee role
					this.executeSQL("UPDATE employeerole"
									+ " SET employeerole.role = '"	+ empRole + "'"
									+ " FROM employeerole"
									+ " JOIN employee ON employee.employeeid = employeerole.employeeid"
									+ " WHERE employee.userid = '" + userId + "'", 'U');
				
				} else {
	
					// generate a new employeeid
					String	employeeId	=  genNewId("employee");
				
					// insert employee
					this.executeSQL("INSERT INTO employee"
							+ " ( employeeid, userid, name, parttime)"
							+ " VALUES "
							+ " ( '" + employeeId + "', '" + userId  + "', '" + name  + "', '" + partTime + "' )",'I');
					
					// insert employee role
					this.executeSQL("INSERT INTO employeerole"
									+ " ( employeeid, role )"
									+ " VALUES"
									+ " ( '" + employeeId + "', '" + empRole + "' )", 'I');
				}
			}

		} catch (DataAccessException | SQLException e) {
			throw new DataAccessException ("Failed to update details. " + e.getMessage());
		} 

		return true;
	}


	// get details for a user
	public User getUserDetails(String userId) 
			throws DataAccessException {
		
		User u = null;
		
		try {
			if (isUserCustomer(userId)) {
				
				u = getCustomerDetails(userId);
			
			} else if (isUserEmployee(userId)) {
				
				u = getEmployeeDetails(userId);
			}

		} catch (DataAccessException e) {
			throw new DataAccessException (e.getMessage());
		}
		
		return u;
	}


	// get customer details
	public Customer getCustomerDetails(String userId) 
			throws DataAccessException {

		Customer c = null;

		try {
			
			String sql = "SELECT "
					+ " user.password"
					+ ",customer.name"
					+ ",customer.emailAddress"
					+ ",customer.type"
					+ ",customer.income"
					+ ",customer.occupation"
					+ ",customer.renthistorymonths"
					+ ",customer.rentalcontractref"
					+ ",customer.employername"
					+ ",customer.employmentmonths"										
					+ "	FROM customer"
					+ " JOIN user ON user.userid = customer.userid"
					+ " WHERE customer.userid = '" + userId + "'";

			ResultSet r = executeSQL(sql, 'Q');
			
			int count = 0;
			
			// moves pointer to first row; return exception when no rows are selected
			if (r.next()) {
				
				count++;
				
				String	userPassword		= r.getString("password");
				String	userName 			= r.getString("name");
				String	userEmail			= r.getString("emailAddress");
				char	type				= r.getString("type").charAt(0);
				double	income				= r.getDouble("income");
				String	occupation			= r.getString("occupation");
				int		renthistorymonths	= r.getInt("renthistorymonths");
				String	rentalcontractref	= r.getString("rentalcontractref");
				String	employername		= r.getString("employername");				
				int		employmentmonths	= r.getInt("employmentmonths");
				
				switch(type) {
					
					case BUY:	c = new Buyer(userId, userPassword, userName, userEmail, type);
								break;
					
					case SELL:	c = new Vendor(userId, userPassword, userName, userEmail, type);
								break;
					
					case RENT:	c = new Tenant(userId, userPassword, userName, userEmail, type);
								// update remaining data specific for tenant
								c.setIncome(income);
								c.setOccupation(occupation);
								c.setRentHistoryMonths(renthistorymonths);
								c.setRentalContractRef(rentalcontractref);
								c.setEmployerName(employername);
								c.setEmploymentMonths(employmentmonths);
								break;
					
					case LEASE:	c = new Landlord(userId, userPassword, userName, userEmail, type);	
								break;					
				}
			}

			if (count == 0) {
				throw new DataAccessException ("No details retrieved for customer " + userId + ".");
			}
			
		} catch (SQLException | DataAccessException | UserException | NullPointerException e) {
			throw new DataAccessException ("Failed to retrieve customer details. " + e.getMessage());
		}		
		
		return c;
	}


	// get employee details
	public Employee getEmployeeDetails(String userId) 
			throws DataAccessException {

		Employee e = null;

		try {
			
			ResultSet r = executeSQL("SELECT "
									+ " user.userid"
									+ ",user.password"
									+ ",employee.name"
									+ ",employee.partTime"
									+ ",employeerole.role"
									+ "	FROM user"
									+ " JOIN employee ON employee.userid = user.userid"
									+ " JOIN employeerole ON employeerole.employeeid = employee.employeeid"
									+ " WHERE user.userid = '" + userId + "'", 'Q');

			
			int count = 0;

			// moves pointer to first row; return exception when no rows are selected
			if (r.next()) {
				
				count++;
				
				String	empPassword	= r.getString("password");
				String	empName 	= r.getString("name");
				char	empPartTime	= r.getString("partTime").charAt(0);
				char	empRole		= r.getString("role").charAt(0);
				
				// create employee object
				e = new Employee(userId, empPassword, empName, "", empRole);
				
				// update remaining instances
				e.setPartTime(empPartTime);
			}
			
		if (count == 0) {
			throw new DataAccessException ("No details retrieved for employee " + userId + ".");
		}

		} catch (SQLException | DataAccessException dae) {
			throw new DataAccessException ("Failed to retrieve employee details.");
		}		
		
		return e;
	}


	// get applications for user
	public HashMap<String, Application> getApplicationCustomer(String userId ) 
						throws DataAccessException {
		
		String sql = "SELECT * FROM application "
				+ "JOIN applicationcustomers ON applicationcustomers.application_id = application.application_id "
				+ "JOIN customer ON customer.customer_id = applicationcustomers.customer_id "
				+ "WHERE customer.userId ='" + userId + "'";
		
		return this.getApplication(sql);
	}	

	
	// get applications for property
	public HashMap<String, Application> getApplicationProperty(String propertyId ) 
						throws DataAccessException {
		
		String sql = "SELECT * FROM application WHERE propertyId = '" + propertyId + "'";
		
		return this.getApplication(sql);
	}	
	
	
	// get applications
	private HashMap<String, Application> getApplication(String sql) 
			throws DataAccessException {
		
		HashMap<String, Application> applicationList = new HashMap<String, Application>();
		
		try {
			ResultSet r = executeSQL(sql, 'Q');

			// moves pointer to first row; return exception when no rows are selected
			while(r.next()) {
				Application a = new Application();
	
				a.setApplicationId(r.getString("applicationid"));
				a.setPropertyId(r.getString("propertyid"));
				a.setWeeklyRentalAmount(r.getDouble("weeklyrentalamount"));
				a.setContractduration(r.getInt("contractduration"));
				a.setApplicationStatus(r.getString("applicationstatus"));
				a.setNoOfApplications(r.getInt("numberofapplicants"));
				
				String d = r.getTimestamp("applicationDate").toString();

				int day		= Integer.parseInt(d.substring(8,2));
				int month	= Integer.parseInt(d.substring(5,2));
				int year	= Integer.parseInt(d.substring(0,3));

				a.setApplicationDate(new DateTime(day, month, year));
	
				applicationList.put(a.getApplicationId(), a);
			}

		} catch (SQLException | DataAccessException dae) {
			throw new DataAccessException ("Failed to retrieve offer details.");
		}	
		
		return applicationList;
	}

	
	// get offers for user
	public HashMap<String, Offer> getOffersCustomer(String userId ) 
						throws DataAccessException {
		String sql = "SELECT * FROM offer "
				+ "JOIN customer ON customer.customer_id = offer.customer_id "
				+ "WHERE customer.userId ='" + userId + "'";
		
		return this.getOffer(sql);
	}	

	
	// get offers for property
	public HashMap<String, Offer> getOffersProperty(String propertyId ) 
						throws DataAccessException {
		String sql = "SELECT * FROM offers WHERE propertyId = '" + propertyId + "'";

		return this.getOffer(sql);
	}		

	
	// get offers
	private HashMap<String, Offer> getOffer(String sql) 
			throws DataAccessException {
		
		HashMap<String, Offer> offerList = new HashMap<String, Offer>();
		
		try {
			ResultSet r = executeSQL(sql, 'Q');

			// moves pointer to first row; return exception when no rows are selected
			while(r.next()) {
	
				Offer o = new Offer();
				String[] dateArr = r.getDate("offerDate").toString().split("-");

				DateTime d = new DateTime(Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[0]));
	
				o.setOfferID(r.getString("offerid"));
				o.setPropertyId(r.getString("propertyid"));
				o.setCustomerID(r.getString("customerid"));
				o.setOfferAmount(r.getDouble("offeramount"));
				o.setOfferDate(d);
				o.setOfferStatus(r.getString("offerstatus").charAt(0));
	
				offerList.put(o.getOfferID(), o);
			}

		} catch (SQLException | DataAccessException dae) {
			throw new DataAccessException ("Failed to retrieve offer details.");
		}		

		return offerList;
	}


	// get property details
	protected HashMap<String, Property> getPropertyDetails(String searchPropertyId, String searchSuburb, 
													int searchMinPrice, int searchMaxPrice, char type,
													String userId ) 
							throws DataAccessException {

		HashMap<String, Property> propertyList = new HashMap<String, Property>();

		try {
			
			String sql = "SELECT * FROM property";
			String where = "";

			String existsSale = " exists ( select 1 from propertysaledetails "
					+ " where propertysaledetails.propertyid = property.propertyid "
					+ " and propertysaledetails.salepricereserve between " + searchMinPrice + " and " + searchMaxPrice + ")";

			String existsRent = " exists ( select 1 from propertyrentaldetails "
					+ " where propertyrentaldetails.propertyid = property.propertyid) "
					+ " and propertyrentaldetails.weeklyrentalreserve between " + searchMinPrice + " and " + searchMaxPrice + ")";

			String existsCustomer = " exists ( select 1 from propertyowner "
					+ " join customer on customer.customer_id = propertyowner.customer_id "
					+ " where customer.user_id = '" + userId + "')";

			// add search by property Id
			if (searchPropertyId != null && searchPropertyId != "") {
				where = "propertyId = '" + searchPropertyId + "'"; 
			}
			
			// add search by suburb
			if (searchSuburb != null && searchSuburb != "") {
				if (where != "") {
					where += " and ";
				}

				where += " suburb = '" + searchSuburb + "'"; 
			}

			// add search by price range
			if (searchMinPrice > 0 || searchMaxPrice > 0) {
				if (where != "") {
					where += " and ";
				}
				
				// search both rent and sale properties
				if (type == SALE) {
					where += existsSale ;
				}

				// search both rent and sale properties
				if (type == RENTAL) {
					where += existsRent;
				}

				// search both rent and sale properties
				if (type =='X') {
					where += "( " + existsSale + " or " + existsRent + " )";
				}
				
				// search property for customer
				if (!userId.isEmpty()) {
					where += "";
				}
			}

			// add search by customer
			if (!userId.isEmpty()) {
				if (where != "") {
					where += " and ";
				}

				where += existsCustomer; 
			}

			if (!where.isEmpty()) {
				sql += " where " + where;
			}
			
			ResultSet r = executeSQL(sql, 'Q');

			// moves pointer to first row; return exception when no rows are selected
			while(r.next()) {

				Property p = (type == SALE) ? new SaleProperty() : new RentalProperty();

				p.setPropertyId(r.getString("propertyId"));
				p.setPropertyAddress(r.getString("address"));
				p.setSuburb(r.getString("suburb"));
				p.setNumbOfBathrooms(r.getInt("bedroom"));
				p.setNumbOfBathrooms(r.getInt("bathroom"));
				p.setNumbOfCarSpace(r.getInt("carSpace"));
				p.setPropertyType(Character.toString(r.getString("type").charAt(0)));
				p.setSection32(r.getString("section32").charAt(0));
				p.setPropertyStatus(r.getString("status"));
				p.setApproved(r.getString("approved").charAt(0));

				propertyList.put(p.getPropertyId(), p);
			}

		} catch (SQLException | DataAccessException dae) {
			throw new DataAccessException ("Failed to retrieve property details.");
		}		
		
		return propertyList;
	}
	
	
	// get user group for a user
	public char getUserGroup(String userId) 
			throws DataAccessException {
		
		char returnVal = PUBLIC;
		
		try {
			if (checkUserExists(userId)) {
				if (isUserCustomer(userId)) {
					returnVal = CUSTOMER;
				} else if (isUserEmployee(userId)) {
					returnVal = EMPLOYEE;
				}
			}
		
		} catch (DataAccessException e) {
			throw new DataAccessException (e.getMessage());
		}
		
		return returnVal;
	}
	
	
	// check that a user exists
	public boolean checkUserExists(String userId) {
	
		boolean returnVal = false;

		try {
			
			ResultSet r = executeSQL("SELECT userid FROM user WHERE userid = '" + userId + "'", 'Q');
			
			while (r.next()) {
				returnVal = true;
			}

		} catch (DataAccessException | SQLException e) {
			
			System.out.println(e.getMessage());
			returnVal = false;
		}

		return returnVal;
	}

	
	public boolean isUserCustomer(String userId) 
			throws DataAccessException {
	
		boolean returnVal = false;

		try {
			
			ResultSet r = executeSQL("SELECT userid FROM customer WHERE userid = '" + userId + "'", 'Q');
				
			// moves pointer to first row; return exception when no rows are selected
			if (r.next()) {
				returnVal = true;
			}

		} catch (SQLException | DataAccessException e) {
			throw new DataAccessException ("Customer not held on file. " + e.getMessage());
		}
		
		return returnVal;
	}


	public boolean isUserEmployee(String userId) 
			throws DataAccessException {
		
		boolean returnVal = false;

		try {
			
			ResultSet r = executeSQL("SELECT userid FROM employee WHERE userid = '" + userId + "'", 'Q');
				
			// moves pointer to first row; return exception when no rows are selected
			if (r.next()) {
				returnVal = true;
			}

		} catch (SQLException | DataAccessException e) {
			throw new DataAccessException ("Employee not held on file. " + e.getMessage());
		}
		
		return returnVal;
	}
	

	// get a result set
	private ResultSet executeSQL(String sqlQuery, char action) 
				throws DataAccessException, SQLException {

		ResultSet		r		= null;
		Statement		s		= null;
		Connection		con		= null;

		try {

			sqlQuery = sqlQuery.toUpperCase();
			
			con	= this.getConnection();
			s	= con.createStatement();
				
			switch(action) {
				case 'I':
				case 'D':
				case 'U':	s.executeUpdate(sqlQuery);
							break;
				
				case 'Q':	r = s.executeQuery(sqlQuery);
							break;

				default :	throw new DataAccessException ("Unknown database action requested.");

			}
			
		} catch( DataAccessException | SQLException e) {
			throw new DataAccessException (e.getMessage());

		} 

		finally {

			try {
				s.close();
				con.close();
			} 
			
			catch (SQLException se) {
				throw new SQLException (se.getMessage());
			}
		}

		return r;
	}
	
	private Connection getConnection()
			throws DataAccessException, SQLException {

		Connection con		= null;

		try {
			// load JDBC driver for HSQLDb
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			// System.out.println("Driver loaded");
			
			// open the database connection
			con = DriverManager.getConnection
					("jdbc:hsqldb:file:database/" + DB_NAME, DB_USER, DB_PASSWORD);
			// System.out.println("Database connected.");
		}

		catch (SQLException se) {
			throw new SQLException (se.getMessage());
		}

		catch (ClassNotFoundException ce) {
			throw new DataAccessException (ce.getMessage());
		}
		
		return con;
	}
	

	public void addData() throws DataAccessException {

		Statement		s		= null;
		Connection		con		= null;

		try {
			
			con	= this.getConnection();
			s	= con.createStatement();
			
			System.out.println("Adding user details.");

			s.addBatch("insert into user values ( '00001', '123')");
			s.addBatch("insert into user values ( '00002', '123')");
			s.addBatch("insert into user values ( '00003', '123')");
			s.addBatch("insert into user values ( '00004', '123')");
			s.addBatch("insert into user values ( '00005', '123')");
			s.addBatch("insert into user values ( '00006', '123')");
			s.addBatch("insert into user values ( '00007', '123')");
			s.addBatch("insert into user values ( '00008', '123')");
			
			s.executeBatch();
			
			System.out.println("Adding employee details.");

			s.addBatch("insert into employee values ( '00001', '00001', 'john', 'john@sec.com', 'N')");			
			s.addBatch("insert into employee values ( '00002', '00002', 'keith', 'keith@sec.com', 'Y')");			
			s.addBatch("insert into employee values ( '00003', '00003', 'tom', 'tom@sec.com', 'N')");			
			s.addBatch("insert into employee values ( '00004', '00004', 'jk', 'jk@sec.com', 'Y')");			

			s.executeBatch();

			System.out.println("Adding employee roles.");

			s.addBatch("insert into employeerole values ( '00001', 'B')");				
			s.addBatch("insert into employeerole values ( '00002', 'S')");				
			s.addBatch("insert into employeerole values ( '00003', 'A')");				
			s.addBatch("insert into employeerole values ( '00004', 'P')");				

			s.executeBatch();

			System.out.println("Adding employee hours.");

			s.addBatch("insert into employeehours values ( '00001', 2019, 10, 0, 'N', 'N')");				
			s.addBatch("insert into employeehours values ( '00002', 2019, 10, 80, 'Y', 'Y')");		
			s.addBatch("insert into employeehours values ( '00003', 2019, 10, 0, 'N', 'N')");			
			s.addBatch("insert into employeehours values ( '00004', 2019, 10, 95, 'Y', 'Y')");
			
			s.executeBatch();

			System.out.println("Adding employee commission.");

			s.addBatch("insert into employeecommission values ( '00002', 2019, 10, 1, 1000, 'Y')");	
			
			s.executeBatch();

			System.out.println("Adding customer details.");
			
			s.addBatch("insert into customer values ( '00001', '00005', 'jen', 'jen@sec.com', 'T', 5400, 'engineer', 3, 1232, 'google', 3)");			
			s.addBatch("insert into customer values ( '00002', '00006', 'ha', 'ha@sec.com', 'B', 2500, '', 3, 1232, '', 0)");			
			s.addBatch("insert into customer values ( '00003', '00007', 'jeff', 'jeff@sec.com', 'V', 0, '', 3, 1232, '', 0)");			
			s.addBatch("insert into customer values ( '00004', '00008', 'sam', 'sam@sec.com', 'L', 0, '', 3, 1232, '', 0)");			

			s.executeBatch();

			System.out.println("Adding customer interests.");
			
			s.addBatch("insert into customerinterests values ( '00001', '00001')");			

			s.executeBatch();

			System.out.println("Adding application customers.");
			
			s.addBatch("insert into applicationcustomers values ( '00001', '00001')");
			
			s.executeBatch();

			System.out.println("Adding applications.");
			
			s.addBatch("insert into application values ( '00001', '00001', 350, 12, 'H', 1, '2019-06-10')");
			
			s.executeBatch();

			System.out.println("Adding offers.");
			
			s.addBatch("insert into offer values ( '00001', '00001', '00002', 200000, '2019-06-10', 'H')");
			
			s.executeBatch();

			System.out.println("Adding property owners.");
			
			s.addBatch("insert into propertyowner values ( '00003', '00003')");
			
			s.executeBatch();

			System.out.println("Adding property details.");
			
			s.addBatch("insert into property values ( '00001', 'testaddress001', 'Moonee Ponds'	, 2, 1, 1, 'R', 'Available')");
			s.addBatch("insert into property values ( '00002', 'testaddress002', 'Moonee Ponds'	, 3, 1, 1, 'R', 'Available')");
			s.addBatch("insert into property values ( '00003', 'testaddress003', 'Ascot Vale'	, 1, 1, 1, 'R', 'Available')");
			s.addBatch("insert into property values ( '00004', 'testaddress004', 'Essendon'		, 1, 1, 1, 'S', 'Available')");
			s.addBatch("insert into property values ( '00005', 'testaddress005', 'Moonee Ponds'	, 3, 1, 1, 'S', 'Available')");
			s.addBatch("insert into property values ( '00006', 'testaddress006', 'Moonee Ponds'	, 3, 1, 1, 'S', 'Available')");

			s.executeBatch();
			
			System.out.println("Data added.");
			
		} catch( DataAccessException | SQLException e) {
			throw new DataAccessException (e.getMessage());			
		}
	}
	

	public void createSchema() throws DataAccessException {
		
		String	sqlStr = null;

		try {
			
			HashMap<String, String> schemaMap = this.getSchemaDets();
				 
			System.out.println("");
			System.out.println("Checking Database Schema ..");
			System.out.println("");

			for(Map.Entry<String, String> entry : schemaMap.entrySet()) {

				try {
					// drop table
					sqlStr = "DROP TABLE " + entry.getKey();
					System.out.println(sqlStr);
					this.executeSQL(sqlStr, 'U');
					System.out.println("Execute completed.");
						 
				} catch (DataAccessException dae) {
					System.out.println("Execute not done." + dae.getMessage());
				
				} catch (SQLException se) {
					throw new DataAccessException("Schema creation failed. " + se.getMessage());
				}
				
				// create table
				sqlStr = entry.getValue();
				System.out.println(sqlStr);
				this.executeSQL(sqlStr, 'U');
				System.out.println("Execute completed.");
			}
		}

		catch(DataAccessException dae) {
			throw new DataAccessException ("Execute not done." + dae.getMessage());
		}

		catch (SQLException se) {
			 throw new DataAccessException("Schema creation failed. " + se.getMessage());
		}
	}
	
	private HashMap<String, String> getSchemaDets() {
	   	
		HashMap<String, String> schemaMap = this.schemaDets();
   		
		return schemaMap;
	}
   	 
 
	private HashMap<String, String> schemaDets() {
   		 
		HashMap<String, String> schemaMap = new HashMap<String, String>();

   		schemaMap.put("user"				, "CREATE TABLE user "
   													+ " (userid VARCHAR(5) NOT NULL, password VARCHAR(25) NOT NULL, "
   													+ " PRIMARY KEY (userid))");
 
   		schemaMap.put("customer"			, "CREATE TABLE customer "
   													+ " (customerid VARCHAR(5) NOT NULL, userid VARCHAR(5) NOT NULL, name VARCHAR(100) NOT NULL, "
   													+ " emailaddress VARCHAR(100), type CHAR(1), income NUMERIC(10,2), occupation VARCHAR(100), "
   													+ " renthistorymonths NUMERIC(3), rentalcontractref VARCHAR(5), employername VARCHAR(100),"
   													+ " employmentmonths NUMERIC(3),"
   													+ " PRIMARY KEY (customerid))");  	
   		
   		schemaMap.put("customerinterests"	, "CREATE TABLE customerinterests "
													+ " (customerid VARCHAR(5) NOT NULL, propertyid VARCHAR(5) NOT NULL,"
													+ " PRIMARY KEY (customerid, propertyid))");  		
   		
   		schemaMap.put("property"			, "CREATE TABLE property "
  													+ " (propertyid VARCHAR(5) NOT NULL, address VARCHAR(100) NOT NULL, "
  													+ " suburb VARCHAR(50), bedroom INTEGER, bathroom INTEGER, carspace INTEGER, "
  													+ " type CHAR(1), status VARCHAR(20), "
  													+ " PRIMARY KEY (propertyid))");
  		
   		schemaMap.put("saleproperties"	, "CREATE TABLE saleproperties "
  													+ " (propertyid VARCHAR(5) NOT NULL, "
  													+ " minimumprice NUMERIC(10,2), customerprice NUMERIC(10,2),"
  													+ " commission NUMERIC(10,2),"
  													+ " PRIMARY KEY (propertyid))");
  		
 		
   		schemaMap.put("rentalproperties", "CREATE TABLE rentalproperties "
  													+ " (propertyid VARCHAR(5), managementfee NUMERIC(10,2), weeklyrentalfee NUMERIC(10,2),"
  													+ " rentduration NUMERIC(3),"
  													+ " PRIMARY KEY (propertyid))");
   		
   		schemaMap.put("offer"			, "CREATE TABLE offer "
													+ " (offerid VARCHAR(5) NOT NULL, propertyid VARCHAR(5), "
													+ " customerid VARCHAR(5), offerAmount NUMERIC(10,2), offerdate DATETIME, offerstatus CHAR(1),"
													+ " PRIMARY KEY (offerid))");

   		schemaMap.put("application"			, "CREATE TABLE application "
													+ " (applicationid VARCHAR(5) NOT NULL, propertyid VARCHAR(5), "
													+ " weeklyrentalamount NUMERIC(10,2), contractduration NUMERIC(3),"
													+ " applicationstatus CHAR(1), numberofapplicants NUMERIC(3), applicationdate DATETIME,"
													+ " PRIMARY KEY (applicationid))");
  		
   		schemaMap.put("propertyowner"		, "CREATE TABLE propertyowner "
  													+ " (propertyid VARCHAR(5) NOT NULL, customerid VARCHAR(5) NOT NULL, "
  													+ " PRIMARY KEY (propertyid, customerid))");
  		
   		schemaMap.put("applicationcustomers", "CREATE TABLE applicationcustomers "
  													+ " (applicationid VARCHAR(5) NOT NULL, customerid VARCHAR(5) NOT NULL, "
  													+ " PRIMARY KEY (applicationid, customerid))");
  		
   		schemaMap.put("propertyinspection"	, "CREATE TABLE propertyinspection "
													+ " (inspectionid VARCHAR(5) NOT NULL, propertyid VARCHAR(5) NOT NULL, "
													+ " inspectiondatetimefrom DATETIME, inspectiondatetimeto DATETIME, cancelled CHAR(1), "
													+ " PRIMARY KEY (inspectionid))");
								  		
  		schemaMap.put("employee"			, "CREATE TABLE employee "
   													+ " (employeeid VARCHAR(5) NOT NULL, userid VARCHAR(5) NOT NULL, name VARCHAR(100), "
   													+ " emailaddress VARCHAR(100), parttime CHAR(1), "
   													+ " PRIMARY KEY (employeeid))");
  		
  		schemaMap.put("employeehours"		, "CREATE TABLE employeehours "
   													+ " (employeeid VARCHAR(5) NOT NULL, year INTEGER NOT NULL, month INTEGER NOT NULL, "
   													+ " hoursworked NUMERIC(5,2), approved CHAR(1), paid CHAR(1), "
   													+ " PRIMARY KEY (employeeid, year, month))");
   		
  		schemaMap.put("employeecommission"	, "CREATE TABLE employeecommission "
   													+ " (employeeid VARCHAR(5) NOT NULL, year INTEGER NOT NULL, month INTEGER NOT NULL, "
   													+ " offerid VARCHAR(5), commissionAmount NUMERIC(10,2), paid CHAR(1), "
   													+ " PRIMARY KEY (employeeid, year, month))");
   		
  		schemaMap.put("employeerole"		, "CREATE TABLE employeerole "
   													+ " (employeeid VARCHAR(5) NOT NULL, role CHAR(1) NOT NULL, "
   													+ " PRIMARY KEY (employeeid, role))");
  		return schemaMap;
   	 
	}
}