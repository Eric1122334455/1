package RealEstateTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import RealEstate.DateTime;

class DateTimeTest {
	DateTime s;
	DateTime s1;
	DateTime d;
	
	@Before
	public void datetime() throws Exception
	{
		s=new DateTime(14,9,2019);
		s1=new DateTime(17,9,2019);
	}
	

	
	@Test
	public void testdiffdays() {
		
		s=new DateTime(14,9,2019);
		s1=new DateTime(17,9,2019);
		
		assertEquals(3,d.diffDays(s1,s));
		
	}

}
