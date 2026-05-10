package api.tests;

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

import io.restassured.response.Response;

public class DDBookingTests {

    static String bearer_token;

    public Logger logger;

    @BeforeClass
    public void setup_data() {

        logger = LogManager.getLogger(this.getClass());

        logger.info("===== Booking Test Setup Started =====");

        AuthUtil auth = new AuthUtil();

        bearer_token = auth.getToken();

        logger.info("Bearer Token Generated Successfully");

        logger.info("===== Booking Test Setup Completed =====");
    }

    @Test(
            priority = 1,
            dataProvider = "BookingData",
            dataProviderClass = DataProviders.class
    )
    public void test_booking_crud(
            String firstname,
            String lastname,
            String totalprice,
            String depositpaid,
            String checkin,
            String checkout,
            String additionalneeds) {

        logger.info("========== BOOKING CRUD TEST STARTED ==========");

        // ================= CREATE PAYLOAD =================

        Bookingdates bookingDates = new Bookingdates();

        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        BookingPayload bookingPayload = new BookingPayload();

        bookingPayload.setFirstname(firstname);
        bookingPayload.setLastname(lastname);

        // Convert String to int
        bookingPayload.setTotalprice(Integer.parseInt(totalprice));

        // Convert String to boolean
        bookingPayload.setDepositpaid(Boolean.parseBoolean(depositpaid));

        bookingPayload.setBookingdates(bookingDates);
        bookingPayload.setAdditionalneeds(additionalneeds);

        logger.info("Payload Prepared Successfully");

        // ================= CREATE BOOKING =================

        Response createResponse =
                BookingEndPoints.create_booking(
                        bookingPayload,
                        bearer_token
                );

        createResponse.then().log().all();

        Assert.assertEquals(createResponse.getStatusCode(), 200);

        String bookingId =
                createResponse.jsonPath().getString("bookingid");

        logger.info("Booking Created Successfully");
        logger.info("Booking ID: " + bookingId);

        // ================= READ BOOKING =================

        Response getResponse =
                BookingEndPoints.read_booking(
                        bookingId,
                        bearer_token
                );

        getResponse.then().log().all();

        Assert.assertEquals(getResponse.getStatusCode(), 200);

        logger.info("Booking Read Successfully");

        // ================= UPDATE BOOKING =================

        bookingPayload.setFirstname(firstname + "_Updated");
        bookingPayload.setLastname(lastname + "_Updated");

        bookingPayload.setTotalprice(
                Integer.parseInt(totalprice) + 100
        );

        bookingPayload.setAdditionalneeds("Lunch");

        Response updateResponse =
                BookingEndPoints.update_booking(
                        bookingId,
                        bookingPayload,
                        bearer_token
                );

        updateResponse.then().log().all();

        Assert.assertEquals(updateResponse.getStatusCode(), 200);

        logger.info("Booking Updated Successfully");

        // ================= DELETE BOOKING =================

        Response deleteResponse =
                BookingEndPoints.delete_booking(
                        bookingId,
                        bearer_token
                );

        deleteResponse.then().log().all();

        Assert.assertEquals(deleteResponse.getStatusCode(), 201);

        logger.info("Booking Deleted Successfully");

        logger.info("========== BOOKING CRUD TEST COMPLETED ==========");
    }
}