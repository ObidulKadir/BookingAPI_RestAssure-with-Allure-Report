package api.tests;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.BookingEndPoints;
import api.payload.BookingPayload;
import api.payload.Bookingdates;
import api.utilities.AuthUtil;
import api.utilities.DataProviders;

import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DDBookingTests {

    static String bearer_token;
    public Logger logger;
    
    // Key: Firstname+Lastname, Value: BookingID
    public static Map<String, String> bookingIdMap = new HashMap<>();

    @BeforeClass
    public void setup_data() {
        logger = LogManager.getLogger(this.getClass());
        RestAssured.filters(new AllureRestAssured());
        bearer_token = getToken();
        logger.info("===== Setup Completed =====");
    }

    // 1. CREATE TEST
    @Test(priority = 1, dataProvider = "BookingData", dataProviderClass = DataProviders.class)
    @Description("Create Booking for all rows in Excel")
    public void test_create_bookings(String fname, String lname, String price, String deposit, String checkin, String checkout, String needs) {
        
        BookingPayload payload = createPayload(fname, lname, price, deposit, checkin, checkout, needs);
        Response response = BookingEndPoints.create_booking(payload, bearer_token);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        
        String bId = response.jsonPath().getString("bookingid");
        bookingIdMap.put(fname + lname, bId);
        
        logger.info("Created ID for "+fname +  " : " +bId);
    }

    // 2. READ TEST (Depends on Create)
    @Test(priority = 2, dataProvider = "BookingData", dataProviderClass = DataProviders.class, dependsOnMethods = "test_create_bookings")
    public void test_get_bookings(String fname, String lname, String price, String deposit, String checkin, String checkout, String needs) {
        
        String bId = bookingIdMap.get(fname + lname);
        Response response = BookingEndPoints.read_booking(bId, bearer_token);
        
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Read successful for ID: " + bId);
    }

    // 3. UPDATE TEST (Depends on Create)
    @Test(priority = 3, dataProvider = "BookingData", dataProviderClass = DataProviders.class, dependsOnMethods = "test_create_bookings")
    public void test_update_bookings(String fname, String lname, String price, String deposit, String checkin, String checkout, String needs) {
        
        String bId = bookingIdMap.get(fname + lname);
        BookingPayload payload = createPayload(fname, lname, price, deposit, checkin, checkout, needs);
        
        // Modifying payload for update
        payload.setFirstname(fname + "_Updated");
        
        Response response = BookingEndPoints.update_booking(bId, payload, bearer_token);
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Updated ID: " + bId);
    }

    // 4. DELETE TEST (Depends on Update)
    @Test(priority = 4, dataProvider = "BookingData", dataProviderClass = DataProviders.class, dependsOnMethods = "test_update_bookings")
    public void test_delete_bookings(String fname, String lname, String price, String deposit, String checkin, String checkout, String needs) {
        
        String bId = bookingIdMap.get(fname + lname);
        Response response = BookingEndPoints.delete_booking(bId, bearer_token);
        
        Assert.assertEquals(response.getStatusCode(), 201);
        logger.info("Deleted ID: " + bId);
    }

    // ================= HELPER METHODS =================
    
    public String getToken() {
        return new AuthUtil().getToken();
    }

    public BookingPayload createPayload(String fn, String ln, String pr, String dp, String ci, String co, String an) {
        Bookingdates dates = new Bookingdates();
        dates.setCheckin(ci);
        dates.setCheckout(co);

        BookingPayload p = new BookingPayload();
        p.setFirstname(fn);
        p.setLastname(ln);
        p.setTotalprice(Integer.parseInt(pr));
        p.setDepositpaid(Boolean.parseBoolean(dp));
        p.setBookingdates(dates);
        p.setAdditionalneeds(an);
        return p;
    }
}