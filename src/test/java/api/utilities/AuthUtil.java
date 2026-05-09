package api.utilities;

import api.endpoints.AuthEndpoints;
import api.payload.Auth;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthUtil {
	public static String getToken() {
		
		 RestAssured.filters(
	                new AllureRestAssured()
	        );

        // 🔹 Create payload
        Auth payload = new Auth();
        payload.setUsername("admin");

        payload.setPassword("password123");

        // 🔹 Step 1: Login
       Response response = AuthEndpoints.login_user(payload);

        return response.jsonPath().getString("token");
    }

}
