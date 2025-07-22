package com.api.framework.users.tests;

import com.api.framework.restutils.RestUtils;
import com.api.framework.users.listeners.TestListener;
import com.api.framework.users.pojo.User;
import com.api.framework.users.pojo.UserResponse;
import com.api.framework.users.testUtils.AssertUtils;
import com.api.framework.utils.ConfigReader;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Listeners(TestListener.class)
@Epic("User Module")
@Feature("Update User Feature")
public class UpdateUserTest {

    @Test(groups = {"sanity", "regression"})
    @Story("Update user by id using put method")
    public void updateUserByIdPutMethod() {
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

        user = User
                .builder()
                .name(faker.name().name())
                .email(faker.internet().emailAddress())
                .gender(faker.gender().binaryTypes().toLowerCase())
                .status("inactive")
                .build();

        Response updateResponse = RestUtils.put(url + "/" + createResponse.getBody().jsonPath().getInt("id"), user, headers);
        AssertUtils.assertEquals(updateResponse.getStatusCode(), 200, "status code valid");
        updateResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schema/user_schema.json"));

        User userUpdateResponse = updateResponse.as(User.class);
        AssertUtils.assertEquals(userUpdateResponse, user, "update user validation success");
        AssertUtils.assertEquals(createResponse.getBody().jsonPath().getInt("id"), updateResponse.getBody().jsonPath().getInt("id"), "Create and update id is correct");

        Response getUserResponse = RestUtils.get(url + "/" + createResponse.getBody().jsonPath().getInt("id"), headers, null);
        AssertUtils.assertEquals(getUserResponse.getStatusCode(), 200, "status code valid");
        AssertUtils.assertEquals(getUserResponse.as(UserResponse.class), updateResponse.as(UserResponse.class), "get API data is correct after update");
    }


    @Test(groups = {"sanity", "regression"})
    @Story("Update user by id using patch method")
    public void updateUserByIdPatchMethod() {
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

        User user1 = User
                .builder()
                .name(faker.name().name())
                .status("inactive")
                .build();

        Response updateResponse = RestUtils.patch(url + "/" + createResponse.getBody().jsonPath().getInt("id"), user1, headers);
        AssertUtils.assertEquals(updateResponse.getStatusCode(), 200, "status code valid");
        updateResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schema/user_schema.json"));

        AssertUtils.assertEquals(createResponse.getBody().jsonPath().getInt("id"), updateResponse.getBody().jsonPath().getInt("id"), "Create and update id is correct");

        Response getUserResponse = RestUtils.get(url + "/" + createResponse.getBody().jsonPath().getInt("id"), headers, null);
        AssertUtils.assertEquals(getUserResponse.getStatusCode(), 200, "status code valid");
        AssertUtils.assertEquals(getUserResponse.as(UserResponse.class), updateResponse.as(UserResponse.class), "get API data is correct after update");
    }
}