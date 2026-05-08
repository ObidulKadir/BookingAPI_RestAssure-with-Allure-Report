package api.endpoints;

import static io.restassured.RestAssured.*;

import api.payload.Auth;
import api.payload.BookingPayload;
import io.restassured.response.Response;

public class BookingEndPoints {
	
	// 0. Auth
    public static Response auth(Auth payload) {

        Response response = given()
           
                .contentType("application/json")
                .accept("application/json")
                .body(payload)

                .when()
                .post(Roots.auth_url);

        return response;
    }

    // 1. Create booking
    public static Response create_booking(BookingPayload payload) {

        Response response = given()
           
                .contentType("application/json")
                .accept("application/json")
                .body(payload)

                .when()
                .post(Roots.create_booking_url);

        return response;
    }

    // 2. Read booking by ID
    public static Response read_booking(int bookingId) {

        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .pathParam("bookingId", bookingId)

                .when()
                .get(Roots.read_booking_url);

        return response;
    }

    // 3. Update booking information
    
    public static Response update_booking(int bookingId, BookingPayload payload) {
    	Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .pathParam("bookingId", bookingId)
                .body(payload)

                .when()
                .put(Roots.update_booking_url);

        return response;
    }
  

    // 4. Delete booking
    public static Response delete_booking(int bookingId) {

        Response response = given()
             
                .contentType("application/json")
                .accept("application/json")
                .pathParam("bookingId", bookingId)

                .when()
                .delete(Roots.delete_booking_url);

        return response;
    }
}