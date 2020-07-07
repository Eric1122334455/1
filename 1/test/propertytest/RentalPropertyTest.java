package RealEstateTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import RealEstate.RentalProperty;

class RentalPropertyTest {
	private RentalProperty rp;
	
	@Before
	public  void rentalprop() throws Exception
	{
		rp=new RentalProperty("166 barkley Street","Footscray",4,2,1,'R',"Available",100,6);
	}

	@Test
	public void testmanagementFee() {
		rp=new RentalProperty("166 barkley Street","Footscray",4,2,1,'R',"Available",100,6);


assertEquals(32,rp.managementFeeforSingleProperty(100));
	}

}