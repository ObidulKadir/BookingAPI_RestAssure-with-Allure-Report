package api.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.AuthEndpoints;
import api.payload.Auth;
import api.utilities.DataProviders;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class DDAuthTests {

	Faker faker;
	Auth authPayload;
	String bearer_token;
	public Logger logger;


	@Test(priority = 1, dataProvider = "AuthData", dataProviderClass = DataProviders.class)
	public void testLoginUser(String username, String password) {
		
		logger = LogManager.getLogger(this.getClass());
		logger.debug("------------Debuging--------------");
		logger.info("===== Setup Data Started =====");
		
		   RestAssured.filters(
	                new AllureRestAssured()
	        );

		logger.info("************* Login User ***************");
		
		Auth authPayload = new Auth();
		authPayload.setUsername(username);
		authPayload.setPassword(password);

		Response response = AuthEndpoints.login_user(authPayload);

		logger.info("Request sent for user login");
		response.then().log().all();

		logger.info("Validating response for user login.");

		Assert.assertEquals(response.getStatusCode(), 200);
		bearer_token = response.jsonPath().getString("token"); //bearer token

		logger.info("************* User logged  Successfully and token successfully.***************");

		
		// schema validator
		response.then().assertThat()
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("auth_login_user_response_schema.json"));

		logger.info("************* Response Json validate successfully ***************");
	}

}
