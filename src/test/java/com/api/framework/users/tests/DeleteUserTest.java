package com.api.framework.users.tests;

import com.api.framework.restutils.RestUtils;
import com.api.framework.users.listeners.TestListener;
import com.api.framework.users.pojo.User;
import com.api.framework.users.testUtils.AssertUtils;
import com.api.framework.utils.ConfigReader;
import com.api.framework.utils.RandomDataGenerator;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Listeners(TestListener.class)
@Epic("User Module")
@Feature("Delete User Feature")
public class DeleteUserTest {

    @Test(groups = {"sanity", "regression"})
    @Story("Delete user by id positive")
    public void deleteUserValid() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Bearer " + RestUtils.getAccessToken());

        Faker faker = new Faker();
        User user = User
                .builder()
                .name(faker.name().name())
                .email(faker.internet().emailAddress())
                .gender(faker.gender().binaryTypes().toLowerCase())
                .status("active")
                .build();

        String url = ConfigReader.get("baseUrl") + "/users";
        Response createResponse = RestUtils.performPost(ConfigReader.get("baseUrl") + "/users",
                user,
                headers);
        AssertUtils.assertEquals(createResponse.getStatusCode(), 201, "status code valid");
        createResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schema/user_schema.json"));

        User userResponse = createResponse.as(User.class);
        AssertUtils.assertEquals(userResponse, user, "Create use validation success");

        Response deleteResponse = RestUtils.delete(url + "/" + createResponse.getBody().jsonPath().getInt("id"), headers);
        AssertUtils.assertEquals(deleteResponse.getStatusCode(), 204, "status code valid");

        Response getUserResponse = RestUtils.get(url + "/" + createResponse.getBody().jsonPath().getInt("id"), headers, null);
        AssertUtils.assertEquals(getUserResponse.getStatusCode(), 404, "deleted user not found");
    }


    @Test(groups = {"sanity", "regression"}, dataProvider = "deleteIds")
    @Story("Delete user by id negative")
    public void deleteUserInValid(String id) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Bearer " + RestUtils.getAccessToken());

        String url = ConfigReader.get("baseUrl") + "/users";

        Response deleteResponse = RestUtils.delete(url + "/" + id, headers);
        AssertUtils.assertEquals(deleteResponse.getStatusCode(), 404, "status code valid");
        if (id == null || !id.isEmpty())
            AssertUtils.assertEquals(deleteResponse.getBody().asString(), "{\"message\":\"Resource not found\"}", "error response");
    }

    @DataProvider(name = "deleteIds")
    public Object[][] userIdForDelete() {
        return new Object[][] {
                {RandomDataGenerator.getRandomNumber(6)},
                {RandomDataGenerator.getRandomNumber(4) + "abcd"},
                {null},
                {""}
        };
    }
}
