package api.tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.BookingEndPoints;
import api.payload.BookingPayload;
import api.payload.Bookingdates;
import api.utilities.AuthUtil;
import io.restassured.response.Response;

import com.github.javafaker.Faker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookingTests {

	Faker faker;
	BookingPayload bookingPayload;

	static String bookingId;
	static String bookingRef;
	static String bearer_token;

	public Logger logger;

	// Payload setup here
	@BeforeClass
	public void setup_data() {
		logger = LogManager.getLogger(this.getClass());
		logger.info("===== Booking Test Setup Started =====");

		faker = new Faker();
		bookingPayload = new BookingPayload();
		
		AuthUtil auth = new AuthUtil();
		bearer_token = auth.getToken();

		// 1. Handle Booking Dates (Usually requires a nested object or Map)
		// Assuming BookingDates is a nested POJO in your BookingPayload
		Bookingdates bookingDates = new Bookingdates();
		bookingDates.setCheckin("2026-01-01");
		bookingDates.setCheckout("2026-01-10");

		// 2. Set realistic data using Faker
		bookingPayload.setFirstname(faker.name().firstName());
		bookingPayload.setLastname(faker.name().lastName());
		bookingPayload.setTotalprice(faker.number().numberBetween(100, 500));
		bookingPayload.setDepositpaid(true);
		bookingPayload.setBookingdates(bookingDates);
		bookingPayload.setAdditionalneeds("Breakfast");

		// Note: The bearer_token should be used in the REQUEST HEADER,
		// not inside the bookingPayload body.

		logger.info("Payload Prepared for User: " + bookingPayload.getFirstname() + " " + bookingPayload.getLastname());
		logger.info("Total Price: " + bookingPayload.getTotalprice());
		logger.info("===== Booking Test Setup Completed =====");
	}

	// 1. Create Booking
	@Test(priority = 1)
	public void test_create_booking() {

		logger.info("************* Create Booking Test Started *************");

//		bearer_token = (String) context.getSuite().getAttribute("bearer_token");
		logger.info("Using Bearer Token: " + bearer_token);

		Response response = BookingEndPoints.create_booking(bookingPayload,bearer_token);

		logger.info("Create booking request sent");
		response.then().log().all();

		logger.info("Validating create booking response");

		Assert.assertEquals(response.getStatusCode(), 200);
		bookingId = response.jsonPath().getString("bookingid");

		logger.info("Booking Created Successfully");
		logger.info("Booking ID: " + bookingId);
		logger.info("Booking Ref: " + bookingRef);

		logger.info("************* Create Booking Test Completed *************");
	}

	// 2. Read by ID
	@Test(priority = 2, dependsOnMethods = "test_create_booking")
	public void test_read_booking_by_id() {

		logger.info("************* Read Booking By ID Test Started *************");

//		bearer_token = (String) context.getSuite().getAttribute("bearer_token");
		logger.info("Using Bearer Token: " + bearer_token);
		logger.info("Booking ID: " + bookingId);

		Response response = BookingEndPoints.read_booking(bookingId, bearer_token);

		logger.info("Read booking by ID request sent");
		response.then().log().all();

		logger.info("Validating response");

		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("************* Read Booking By ID Test Completed *************");
	}

	// 3. Update Booking
	@Test(priority = 3, dependsOnMethods = "test_create_booking")
	public void test_update_booking() {

		logger.info("************* Update Booking Test Started *************");

		// Update the payload with new data to test the change
		bookingPayload.setFirstname(faker.name().firstName() + "_Updated");
		bookingPayload.setLastname(faker.name().lastName() + "_Updated");
		bookingPayload.setTotalprice(faker.number().numberBetween(500, 1000));
		bookingPayload.setAdditionalneeds("Late Check-out");

		logger.info("Updating Booking ID: " + bookingId);
		logger.info("New Price to set: " + bookingPayload.getTotalprice());

		// Call the endpoint
		Response response = BookingEndPoints.update_booking(bookingId, bookingPayload, bearer_token);

		logger.info("Update booking request sent");
		response.then().log().all();

		logger.info("Validating update response");

		// Assertions (Adjust status code/json path based on your specific API)
		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("Booking Updated Successfully");
		logger.info("************* Update Booking Test Completed *************");
	}

	// 4. Delete Booking
	@Test(priority = 4, dependsOnMethods = "test_create_booking")
	public void test_delete_booking() {

		logger.info("************* Delete Booking Test Started *************");

//		bearer_token = (String) context.getSuite().getAttribute("bearer_token");
		logger.info("Using Bearer Token: " + bearer_token);
		logger.info("Booking ID to delete: " + bookingId);

		Response response = BookingEndPoints.delete_booking(bookingId, bearer_token);

		logger.info("Delete booking request sent");
		response.then().log().all();

		logger.info("Validating delete response");

		Assert.assertEquals(response.getStatusCode(), 201);

		logger.info("Booking Deleted Successfully");

		logger.info("************* Delete Booking Test Completed *************");
	}
}