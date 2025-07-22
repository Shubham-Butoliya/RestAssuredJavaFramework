package com.api.framework.users.tests;

import com.api.framework.users.listeners.RetryAnalyzer;
import com.api.framework.users.listeners.TestListener;
import com.api.framework.users.pojo.User;
import com.api.framework.restutils.RestUtils;
import com.api.framework.users.testUtils.AssertUtils;
import com.api.framework.utils.ConfigReader;
import com.api.framework.utils.ExcelUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Listeners(TestListener.class)
@Epic("User Module")
@Feature("Create User Positive Feature")
public class CreateUserTest {

    @Test(groups = "regression", description = "Verify user creation")
    @Story("Create new user with request as String")
    public void createStringUserTest() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Bearer " + RestUtils.getAccessToken());

        Response response = RestUtils.performPost(ConfigReader.get("baseUrl") + "/users",
                "{\n" +
                        "    \"name\":\"Tenali Ramakrishna\", \n" +
                        "    \"gender\":\"male\", \n" +
                        "    \"email\":\"tenali.ramakrishna@15ce.com\", \n" +
                        "    \"status\":\"active\"\n" +
                        "}",
                headers);
        AssertUtils.assertEquals(response.getStatusCode(), 201, "status code is valid");

        JsonObject jsonResponse = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        AssertUtils.assertEquals(jsonResponse.get("name").getAsString(), "Tenali Ramakrishna", "name is correct");
        AssertUtils.assertEquals(jsonResponse.get("gender").getAsString(), "male", "gender is correct");
        AssertUtils.assertEquals(jsonResponse.get("email").getAsString(), "tenali.ramakrishna@15ce.com", "email is correct");
        AssertUtils.assertEquals(jsonResponse.get("status").getAsString(), "active", "status is correct");
    }

    @Test(groups = {"sanity", "regression"})
    @Story("Create new user with request with random generator")
    public void createRandomDataUserTest() {
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

        Response response = RestUtils.performPost(ConfigReader.get("baseUrl") + "/users",
                user,
                headers);
        AssertUtils.assertEquals(response.getStatusCode(), 201, "status code is valid");
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/user_schema.json"));

        User userResponse = response.as(User.class);
        AssertUtils.assertEquals(userResponse, user, "user is correct");
    }

    @Test(dataProvider = "userData", groups = "regression")
    @Story("Create new user with request from Excel data")
    public void createExcelDataUserTest(User user) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Bearer " + RestUtils.getAccessToken());

        Response response = RestUtils.performPost(ConfigReader.get("baseUrl") + "/users",
                user,
                headers);
        AssertUtils.assertEquals(response.getStatusCode(), 201, "status code is valid");

        User userResponse = response.as(User.class);
        AssertUtils.assertEquals(userResponse, user, "user is correct");
    }

    @Test(dataProvider = "userDataPoiji", retryAnalyzer = RetryAnalyzer.class, groups = "regression")
    @Story("Create new user with request from Excel data using Poiji")
    public void createExcelDataUserPoijiTest(User user) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Bearer " + RestUtils.getAccessToken());

        Response response = RestUtils.performPost(ConfigReader.get("baseUrl") + "/users",
                user,
                headers);
        AssertUtils.assertEquals(response.getStatusCode(), 201, "status code is valid");

        User userResponse = response.as(User.class);
        AssertUtils.assertEquals(userResponse, user, "user is correct");
    }

    @DataProvider(name = "userDataPoiji")
    public Iterator<User> getCreateUserDataPoiji() throws IOException {
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().sheetName("Sheet1").build();
        List<User> userData = Poiji.fromExcel(new File("src/test/resources/testdata/CreateUserData.xlsx"),
                User.class, options);
        return userData.iterator();
    }

    @DataProvider(name = "userData")
    public Iterator<User> getCreateUserData() throws IOException {
        List<LinkedHashMap<String, String>> excelDataAsListOfMap = ExcelUtils.getExcelDataAsListOfMap("CreateUserData", "Sheet1");
        System.out.println(excelDataAsListOfMap);
        List<User> userData = new ArrayList<>();
        for(LinkedHashMap<String,String> data : excelDataAsListOfMap) {
            User user = User.builder()
                    .name(data.get("name"))
                    .gender(data.get("gender"))
                    .email(data.get("email"))
                    .status(data.get("status"))
                    .build();
            userData.add(user);
        }
        return userData.iterator();
    }
}
