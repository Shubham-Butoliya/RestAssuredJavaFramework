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
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Listeners(TestListener.class)
@Epic("User Module")
@Feature("Get User Positive Feature")
public class GetUserTest {

    @Test(groups = {"sanity", "regression"})
    @Story("Get user by id")
    public void getUserById() {
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

        Response getUserResponse = RestUtils.get(url + "/" + createResponse.getBody().jsonPath().getInt("id"), headers, null);
        AssertUtils.assertEquals(getUserResponse.getStatusCode(), 200, "status code valid");
        getUserResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schema/user_schema.json"));
        AssertUtils.assertEquals(getUserResponse.as(UserResponse.class), createResponse.as(UserResponse.class), "get API data is correct");
    }


    @Test(groups = {"sanity", "regression"})
    @Story("Get users list")
    public void getAllUsers() {
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

        User userResponse = createResponse.as(User.class);
        AssertUtils.assertEquals(userResponse, user, "Create use validation success");

        Response allUserResponse = RestUtils.get(url, headers, null);
        AssertUtils.assertEquals(allUserResponse.getStatusCode(), 201, "status code valid");
        allUserResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schema/getAllUserSchema.json"));
        List<UserResponse> users = allUserResponse.jsonPath().getList("", UserResponse.class);

        for (UserResponse userVal : users) {
            if (createResponse.getBody().jsonPath().getInt("id") == userVal.getId()) {
                AssertUtils.assertEquals(userVal, createResponse.as(UserResponse.class), "created user is present in the list");
            }
        }
    }
}
