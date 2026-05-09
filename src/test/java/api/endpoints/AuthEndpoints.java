package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import api.payload.Auth;
import io.restassured.response.Response;

public class AuthEndpoints {
	// action perform method integrate this class.

	
	// . Login user
	public static Response login_user(Auth payload) {
		
		Response response = given()
			.contentType("application/json")
			.accept("application/json")
			.body(payload)
			
		.when()
			.post(Roots.auth_url);
		
		return response ;
	}
	
}
