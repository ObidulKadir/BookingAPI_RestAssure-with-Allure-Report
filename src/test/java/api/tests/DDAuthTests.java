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

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class DDAuthTests {

    Faker faker;
    Auth authPayload;
    String bearer_token;
    public Logger logger;

    @BeforeClass
    public void setup() {
        logger = LogManager.getLogger(this.getClass());

        // Attach Allure filter globally (best practice)
        RestAssured.filters(new AllureRestAssured());

        logger.info("===== Test Setup Completed =====");
    }

    @Test(priority = 1, dataProvider = "AuthData", dataProviderClass = DataProviders.class)
    @Description("Validate user login API with valid credentials")
    public void testLoginUser(String username, String password) {

        logger.info("************* Starting Login Test ***************");

        authPayload = createAuthPayload(username, password);

        Response response = loginUser(authPayload);

        validateResponse(response);

        extractToken(response);

        validateSchema(response);

        logger.info("************* Login Test Completed Successfully ***************");
    }

    // ---------------- Helper Methods ----------------

    @Step("Create auth payload")
    public Auth createAuthPayload(String username, String password) {
        Auth auth = new Auth();
        auth.setUsername(username);
        auth.setPassword(password);
        return auth;
    }

    @Step("Send login request")
    public Response loginUser(Auth authPayload) {
        Response response = AuthEndpoints.login_user(authPayload);
        response.then().log().all();
        return response;
    }

    @Step("Validate response status code is 200")
    public void validateResponse(Response response) {
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Step("Extract bearer token from response")
    public void extractToken(Response response) {
        bearer_token = response.jsonPath().getString("token");
        logger.info("Token extracted successfully: " + bearer_token);
    }

    @Step("Validate JSON schema of login response")
    public void validateSchema(Response response) {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                        "auth_login_user_response_schema.json"));
    }
}