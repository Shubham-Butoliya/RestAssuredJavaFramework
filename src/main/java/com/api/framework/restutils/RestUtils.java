package com.api.framework.restutils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RestUtils {

    private static RequestSpecification getRequestSpecification(String endPoint, Map<String,String>headers) {
        return RestAssured.given()
                .baseUri(endPoint)
                .headers(headers);
    }

    public static String getAccessToken() {
        return "use your token here";
    }

    public static Response get(String endPoint, Map<String,String> headers, Map<String, String> queryParams) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        if (queryParams != null && !queryParams.isEmpty()) {
            requestSpecification.queryParams(queryParams);
        }
        Response response =  requestSpecification.get();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }


    public static Response patch(String endPoint, Object requestPayload, Map<String,String> headers) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        Response response =  requestSpecification.body(requestPayload).patch();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }

    public static Response put(String endPoint, Object requestPayload, Map<String,String> headers) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        Response response =  requestSpecification.body(requestPayload).put();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }

    public static Response delete(String endPoint, Map<String,String> headers) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        Response response =  requestSpecification.delete();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }

    public static Response performPost(String endPoint, String requestPayload, Map<String,String> headers) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        Response response =  requestSpecification.body(requestPayload).post();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }

    public static Response performPost(String endPoint, Map<String, Object> requestPayload, Map<String,String>headers) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        Response response =  requestSpecification.body(requestPayload).post();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }

    public static Response performPost(String endPoint, Object requestPayloadAsPojo, Map<String,String>headers) {
        RequestSpecification requestSpecification = getRequestSpecification(endPoint, headers);
        Response response =  requestSpecification.body(requestPayloadAsPojo).post();
        ApiLoggerUtil.logRequest(requestSpecification);
        ApiLoggerUtil.logResponse(response);
        return response;
    }
}
