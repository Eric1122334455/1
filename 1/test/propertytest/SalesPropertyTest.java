package RealEstateTest;



import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


import RealEstate.SaleProperty;
import RealEstate.SalePropertyException;

public class SalesPropertyTest {
	private SaleProperty sp;
	
	@Before
	public  void rentalprop() throws SalePropertyException
	{
		sp=new SaleProperty("166 barkley Street","Footscray",4,2,1, 200,30,'R',"Available");
	}

	@Test
	public void testmanagementFee() throws SalePropertyException {
		sp=new SaleProperty("166 barkley Street","Footscray",4,2,1, 200,30,'R',"Available");


assertEquals(true,sp.soldProperty(300));
	}
	
	@Test(expected=SalePropertyException.class)
	public void testmanagementFeeInvallid() throws SalePropertyException {
		sp=new SaleProperty("166 barkley Street","Footscray",4,2,1, 200,30,'R',"Available");



	}
	
	
	

}