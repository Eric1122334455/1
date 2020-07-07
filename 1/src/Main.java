import common.GeneralException;
import common.SERealEstateAgency;

public class Main {

	public static void main(String[] args) {

		SERealEstateAgency s = new SERealEstateAgency();
		try {
			s.runApp();
		}
		
		catch (GeneralException ge) {
			System.out.println(ge.getMessage());
			System.out.println("Application Terminated.");
		}
    }
}
