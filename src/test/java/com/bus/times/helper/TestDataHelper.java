package com.bus.times.helper;

public class TestDataHelper {
  public static final String G_STOP_CODE = "490000091G";
  public static final String H_STOP_CODE = "490000091H";
  public static String CLIENT_URL = "https://api.tfl.gov.uk/StopPoint/%s/Arrivals";
  public static final String STOP_H_CLIENT_RESPONSE_PATH =
      "responses/client/get-stop-h-client-response.json";
  public static final String STOP_G_CLIENT_RESPONSE_PATH =
      "responses/client/get-stop-g-client-response.json";
  public static final String GET_BUS_TIME_RESPONSE_PATH =
      "responses/getbustimes/get-bus-times-api-response.json";
  public static final String STOP_G_RESPONSE_MISSING_FIELDS =
      "responses/client/get-stop-g-client-response-missing-fields.json";
}
