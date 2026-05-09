package api.endpoints;

import static io.restassured.RestAssured.*;

import api.payload.BookingPayload;
import io.restassured.response.Response;

public class BookingEndPoints {

    // 1. Create booking (Optional: Added token if your API requires it)
    public static Response create_booking(BookingPayload payload, String token) {

        Response response = given()
//                .header("Authorization", "Bearer " + token) // Adding bearer token
        		.cookie("token", token)
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                .post(Roots.create_booking_url);

        return response;
    }

    // 2. Read booking by ID (Usually public, but added token just in case)
    public static Response read_booking(String bookingId, String token) {

        Response response = given()
        		.cookie("token", token)
                .contentType("application/json")
                .accept("application/json")
                .pathParam("bookingId", bookingId)
                .when()
                .get(Roots.read_booking_url);

        return response;
    }

    // 3. Update booking information (Requires Token)
    public static Response update_booking(String bookingId, BookingPayload payload, String token) {
        Response response = given()
//                .header("Authorization", "Bearer " + token) // Adding bearer token
        		.cookie("token", token)
                .contentType("application/json")
                .accept("application/json")
                .pathParam("bookingId", bookingId)
                .body(payload)
                .when()
                .put(Roots.update_booking_url);

        return response;
    }

    // 4. Delete booking (Requires Token)
    public static Response delete_booking(String bookingId, String token) {

        Response response = given()
//                .header("Authorization", "Bearer " + token) // Adding bearer token
        		.cookie("token", token)
                .contentType("application/json")
                .accept("application/json")
                .pathParam("bookingId", bookingId)
                .when()
                .delete(Roots.delete_booking_url);

        return response;
    }
}