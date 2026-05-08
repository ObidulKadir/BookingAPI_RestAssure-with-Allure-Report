package api.endpoints;

public class Roots {
	
	public static String base_url= "https://restful-booker.herokuapp.com";

	
	//auth
	public static String auth_url = base_url + "/auth";
	
	
	public static String create_booking_url = base_url + "/booking";
	public static String read_booking_url = base_url + "/booking/{bookingId}";
	public static String update_booking_url = base_url + "/booking/{bookingId}";
	public static String delete_booking_url = base_url + "/booking/{bookingId}";
	

}
