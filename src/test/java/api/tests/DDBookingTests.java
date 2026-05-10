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

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DDBookingTests {

    static String bearer_token;
    public Logger logger;

    // ================= SETUP =================
    @BeforeClass
    public void setup_data() {

        logger = LogManager.getLogger(this.getClass());

        logger.info("===== Booking Test Setup Started =====");

        RestAssured.filters(new AllureRestAssured());

        bearer_token = getToken();

        logger.info("Bearer Token Generated Successfully");

        logger.info("===== Booking Test Setup Completed =====");
    }

    // ================= MAIN TEST =================
    @Test(priority = 1,
            dataProvider = "BookingData",
            dataProviderClass = DataProviders.class)
    @Description("Full Booking CRUD flow: Create, Read, Update, Delete")
    public void test_booking_crud(
            String firstname,
            String lastname,
            String totalprice,
            String depositpaid,
            String checkin,
            String checkout,
            String additionalneeds) {

        logger.info("========== BOOKING CRUD TEST STARTED ==========");

        BookingPayload payload = createPayload(
                firstname, lastname, totalprice,
                depositpaid, checkin, checkout, additionalneeds
        );

        String bookingId = createBooking(payload);

        readBooking(bookingId);

        updateBooking(bookingId, payload, firstname, lastname, totalprice);

        deleteBooking(bookingId);

        logger.info("========== BOOKING CRUD TEST COMPLETED ==========");
    }

    // ================= TOKEN =================
    @Step("Get Auth Token")
    public String getToken() {
        AuthUtil auth = new AuthUtil();
        return auth.getToken();
    }

    // ================= CREATE PAYLOAD =================
    @Step("Create Booking Payload")
    public BookingPayload createPayload(
            String firstname,
            String lastname,
            String totalprice,
            String depositpaid,
            String checkin,
            String checkout,
            String additionalneeds) {

        Bookingdates bookingDates = new Bookingdates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        BookingPayload payload = new BookingPayload();
        payload.setFirstname(firstname);
        payload.setLastname(lastname);
        payload.setTotalprice(Integer.parseInt(totalprice));
        payload.setDepositpaid(Boolean.parseBoolean(depositpaid));
        payload.setBookingdates(bookingDates);
        payload.setAdditionalneeds(additionalneeds);

        logger.info("Payload Prepared Successfully");

        return payload;
    }

    // ================= CREATE =================
    @Step("Create Booking")
    public String createBooking(BookingPayload payload) {

        Response response = BookingEndPoints.create_booking(payload, bearer_token);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        String bookingId = response.jsonPath().getString("bookingid");

        logger.info("Booking Created Successfully. ID: " + bookingId);

        return bookingId;
    }

    // ================= READ =================
    @Step("Read Booking")
    public void readBooking(String bookingId) {

        Response response = BookingEndPoints.read_booking(bookingId, bearer_token);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        logger.info("Booking Read Successfully");
    }

    // ================= UPDATE =================
    @Step("Update Booking")
    public void updateBooking(
            String bookingId,
            BookingPayload payload,
            String firstname,
            String lastname,
            String totalprice) {

        payload.setFirstname(firstname + "_Updated");
        payload.setLastname(lastname + "_Updated");
        payload.setTotalprice(Integer.parseInt(totalprice) + 100);
        payload.setAdditionalneeds("Lunch");

        Response response = BookingEndPoints.update_booking(
                bookingId, payload, bearer_token
        );

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        logger.info("Booking Updated Successfully");
    }

    // ================= DELETE =================
    @Step("Delete Booking")
    public void deleteBooking(String bookingId) {

        Response response = BookingEndPoints.delete_booking(
                bookingId, bearer_token
        );

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 201);

        logger.info("Booking Deleted Successfully");
    }
}