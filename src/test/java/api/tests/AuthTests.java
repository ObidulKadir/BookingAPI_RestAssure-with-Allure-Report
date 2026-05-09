package api.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.AuthEndpoints;
import api.payload.Auth;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class AuthTests {

	Faker faker;
	Auth authPayload;
	String bearer_token;
	public Logger logger;

	@BeforeClass
	public void setup_data() {

		logger = LogManager.getLogger(this.getClass());
		logger.debug("------------Debuging--------------");
		logger.info("===== Setup Data Started =====");

		faker = new Faker();
		authPayload = new Auth();

		authPayload.setUsername("admin");
		authPayload.setPassword("password123");

		logger.info("Generated Email: " + authPayload.getUsername());
		logger.info("Generated Password: " + authPayload.getPassword());

		logger.info("===== Setup Data Completed =====");
	}

	@Test(priority = 1)
	public void testLoginUser() {

		logger.info("************* Login User ***************");

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
