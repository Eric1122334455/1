package dataaccess;

import org.junit.*;

import customer.Buyer;
import customer.Customer;
import customer.Landlord;
import customer.Tenant;
import customer.Vendor;
import employee.Employee;

import static org.junit.Assert.*;

public class DataAccessLayerTest {

	DataAccessLayer dal;

	// one time initialisation code
	// here we add the expensive setup
	@BeforeClass
	public static void setUpClass() throws Exception {

	}
	

	// cleanup after tests
	@AfterClass
	public static void tearDownClass() throws Exception {

	}
	
	
	// code to be run before each test
	@Before
	public void setUp() throws Exception {
		dal = new DataAccessLayer();
	}


	// code to be run after each test
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGenUserId() throws DataAccessException  {   
		// generate new userId
		String id = dal.genNewId("user");
		assertEquals(id.isEmpty(), false);
	}
	
	@Test
	public void testGenCustomerId() throws DataAccessException  {   
		// generate new customerId
		String id = dal.genNewId("customer");
		assertEquals(id.isEmpty(), false);
	}
	
	@Test
	public void testGenPropertyId() throws DataAccessException  {   
		// generate new propertyId
		String id = dal.genNewId("property");
		assertEquals(id.isEmpty(), false);
	}
	
	@Test   (expected=DataAccessException.class)
	public void testGenIdNoTable() throws DataAccessException  {   
		// generate new userId
		dal.genNewId("");
	}
	

	@Test    (expected=DataAccessException.class)
	public void testGenIdInvalidTable() throws DataAccessException  {   
		// generate new userId
		dal.genNewId("Manga");
	}
	
	
	@Test
	public void testGetCustomerDetailsTenant() throws DataAccessException  {  
		Customer c = dal.getCustomerDetails("00005");	// customer exists and is a Tenant
		assertEquals((c instanceof Tenant), true);
	}

	@Test
	public void testGetCustomerDetailsBuyer() throws DataAccessException  {  
		Customer c = dal.getCustomerDetails("00006");	// customer exists and is a Buyer
		assertEquals((c instanceof Buyer), true);
	}
	
	@Test
	public void testGetCustomerDetailsVendor() throws DataAccessException  {  
		Customer c = dal.getCustomerDetails("00007");	// customer exists and is a Vendor
		assertEquals((c instanceof Vendor), true);
	}
	
	@Test
	public void testGetCustomerDetailsLandlord() throws DataAccessException  {  
		Customer c = dal.getCustomerDetails("00008");	// customer exists and is a Landlord
		assertEquals((c instanceof Landlord), true);
	}
	
	@Test   (expected=DataAccessException.class)
	public void testGetCustomerDetailsNotExists() throws DataAccessException  {  
		dal.getCustomerDetails("5");		// customer does not exist
	}
	
	
	@Test   (expected=DataAccessException.class)
	public void testGetCustomerDetailsEmployeeId() throws DataAccessException  {  
		dal.getCustomerDetails("00001");	// user id 1 is for an employee
	}
	
	
	@Test
	public void testGetEmployeeDetailsExists() throws DataAccessException  {  
		Employee e = dal.getEmployeeDetails("00001");	// employee exists
		assertEquals(e.getUserId(), "00001");
	}
	
	
	@Test   (expected=DataAccessException.class)
	public void testGetEmployeeDetailsNotExists() throws DataAccessException  {  
		dal.getEmployeeDetails("1");		// employee does not exist
	}
	
	
	@Test   (expected=DataAccessException.class)
	public void testGetEmployeeDetailsCustomerId() throws DataAccessException  {  
		dal.getEmployeeDetails("00005");	// user id 5 is for a customer
	}
	
	@Test  
	public void testLoginValidCustomer() throws DataAccessException  {  
		dal.verifyLogin("00005", "123");	// user id 5 is for a customer
	}	
	
	@Test  
	public void testLoginValidEmployee() throws DataAccessException  {  
		dal.verifyLogin("00001", "123");	// user id 5 is for a customer
	}
	
	@Test     (expected=DataAccessException.class)
	public void testLoginNoPassword() throws DataAccessException  {  
		dal.verifyLogin("00005", "");		// user id 5 is for a customer
	}
	
	@Test  
	public void testLoginInvalidPassword() throws DataAccessException  {  
		assertEquals(dal.verifyLogin("00005", "456"), false);	// user id 5 is for a customer, password is incorrect
	}

	@Test     (expected=DataAccessException.class)
	public void testLoginNoUserId() throws DataAccessException  {  
		dal.verifyLogin("", "123");	
	}
	
	@Test     (expected=DataAccessException.class)
	public void testLoginInvalidUserId() throws DataAccessException  {  
		dal.verifyLogin("99999", "123");
	}
}
