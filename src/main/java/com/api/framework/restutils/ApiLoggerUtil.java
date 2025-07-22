package com.api.framework.restutils;

import com.api.framework.utils.ExtentLogger;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiLoggerUtil {

    public static void logRequest(RequestSpecification requestSpecification) {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        ExtentLogger.logInfo("🔹 <b>Request:</b>");
        ExtentLogger.logInfo("➡️ <b>Method:</b> " + queryableRequestSpecification.getMethod());
        ExtentLogger.logInfo("🌐 <b>URL:</b> " + queryableRequestSpecification.getBaseUri());
        ExtentLogger.logInfo("📋 <b>Headers:</b> " + queryableRequestSpecification.getHeaders().toString().replaceAll("Bearer\\s+[A-Za-z0-9\\-\\._~\\+\\/]+=*", "Bearer *******"));
        if (!queryableRequestSpecification.getQueryParams().isEmpty())
            ExtentLogger.logInfo("📋 <b>Query params:</b> " + queryableRequestSpecification.getQueryParams().toString());

        if (queryableRequestSpecification.getBody() != null)
            ExtentLogger.logJson("📦 Request Body", queryableRequestSpecification.getBody());

        //For allure report only
        Allure.addAttachment("🔹 <b>Request:</b>", "");
        Allure.addAttachment("➡️ <b>Method:</b> " , queryableRequestSpecification.getMethod());
        Allure.addAttachment("➡️ <b>URL:</b> " , queryableRequestSpecification.getBaseUri());
        Allure.addAttachment("📋 <b>Headers:</b> " , queryableRequestSpecification.getHeaders().toString().replaceAll("Bearer\\s+[A-Za-z0-9\\-\\._~\\+\\/]+=*", "Bearer *******"));
        if (!queryableRequestSpecification.getQueryParams().isEmpty())
            Allure.addAttachment("📋 <b>Query params:</b> " , queryableRequestSpecification.getQueryParams().toString());
        if (queryableRequestSpecification.getBody() != null)
            Allure.addAttachment("📦 Request Body", queryableRequestSpecification.getBody().toString());
    }

    public static void logResponse(Response response) {
        ExtentLogger.logInfo("🔸 <b>Response:</b>");
        ExtentLogger.logInfo("📋 <b>Headers:</b> " + response.getHeaders().toString());

        Allure.addAttachment("🔸 <b>Response:</b>", "");
        Allure.addAttachment("📋 <b>Headers:</b> " , response.getHeaders().toString());

        String responseBody = response.getBody().asString();
        // Try parsing only if it looks like JSON
        if (responseBody != null && responseBody.trim().startsWith("{")) {
            try {
                JSONObject json = new JSONObject(responseBody);
                ExtentLogger.logInfo("<b>Status Code:</b> " + response.statusCode());
                ExtentLogger.logInfo("Response Body:\n" + json.toString(2));

                Allure.addAttachment("<b>Status Code:</b> " , response.statusCode()+"");
                Allure.addAttachment("Response Body:\n" , json.toString(2));
            } catch (JSONException e) {
                ExtentLogger.logInfo("<b>Status Code:</b> " + response.statusCode());
                ExtentLogger.logInfo("Invalid JSON Response:\n" + responseBody);
                ExtentLogger.logInfo("Exception: " + e.getMessage());

                Allure.addAttachment("<b>Status Code:</b> " , response.statusCode()+"");
                Allure.addAttachment("Invalid JSON Response:\n" , responseBody);
                Allure.addAttachment("Exception: " , e.getMessage());
            }
        } else {
            ExtentLogger.logInfo("<b>Status Code:</b> " + response.statusCode());
            ExtentLogger.logInfo("Non-JSON Response:\n" + responseBody);

            Allure.addAttachment("<b>Status Code:</b> " , response.statusCode()+"");
            Allure.addAttachment("Non-JSON Response:\n" , responseBody);
        }
    }

}
