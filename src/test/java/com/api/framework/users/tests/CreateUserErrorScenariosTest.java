package com.api.framework.users.tests;

import com.api.framework.restutils.RestUtils;
import com.api.framework.users.listeners.TestListener;
import com.api.framework.users.pojo.ErrorResponse;
import com.api.framework.users.pojo.User;
import com.api.framework.users.testUtils.AssertUtils;
import com.api.framework.utils.ConfigReader;
import com.api.framework.utils.ExcelUtils;
import com.api.framework.utils.ExtentLogger;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

@Listeners(TestListener.class)
@Epic("User Module")
@Feature("Create User Negative Feature")
public class CreateUserErrorScenariosTest {

    @Test(dataProvider = "userData", groups = {"sanity", "regression"})
    @Story("Create new user with invalid details for error testing")
    public void createExcelDataUserTest(User user, ErrorResponse errorResponse) {
        ExtentLogger.logInfo(MarkupHelper.createLabel("Description - " + errorResponse.getScenarioId(), ExtentColor.TEAL));
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Bearer " + RestUtils.getAccessToken());

        Response response = RestUtils.performPost(ConfigReader.get("baseUrl") + "/users",
                user,
                headers);
        AssertUtils.assertEquals(response.getStatusCode(), errorResponse.getStatusCode(), "status code is valid");

        AssertUtils.assertEquals(response.asPrettyString(), errorResponse.getErrorJson(), "error response is correct");
    }

    @DataProvider(name = "userData")
    public Iterator<Object[]> getCreateUserData() throws IOException {
        List<LinkedHashMap<String, String>> excelDataAsListOfMap = ExcelUtils.getExcelDataAsListOfMap("CreateUserData", "Sheet2");
        System.out.println(excelDataAsListOfMap);

        List<Object[]> testData = new ArrayList<>();

        for(LinkedHashMap<String,String> data : excelDataAsListOfMap) {
            User user = User.builder()
                    .name(data.get("name"))
                    .gender(data.get("gender"))
                    .email(data.get("email"))
                    .status(data.get("status"))
                    .build();

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorJson(data.get("errorJson"));
            errorResponse.setStatusCode(data.get("statusCode") != null ? Integer.valueOf(data.get("statusCode")) : null);
            errorResponse.setScenarioId(data.get("scenarioId"));

            testData.add(new Object[]{user, errorResponse});
        }
        return testData.iterator();
    }

}
