package com.bus.times.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.bus.times.helper.JsonLoader.loadJsonFile;
import static com.bus.times.helper.TestDataHelper.*;
import static java.lang.String.format;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class IntegrationTest {

  @Autowired private MockMvc mvc;

  @Autowired private RestTemplate restTemplate;

  private MockRestServiceServer mockServer;

  private static final String GET_API_URL = "/greatPortlandStreetTimes";

  @BeforeEach
  void init() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void shouldReturnBusArrivalTimesForStopGandH() throws Exception {
    mockClientResponse(STOP_G_CLIENT_RESPONSE_PATH, G_STOP_CODE);
    mockClientResponse(STOP_H_CLIENT_RESPONSE_PATH, H_STOP_CODE);

    mvc.perform(get(GET_API_URL))
        .andExpect(status().isOk())
        .andExpect(content().json(loadJsonFile(GET_BUS_TIME_RESPONSE_PATH)));
  }

  @Test
  void shouldReturnBusArrivalTimesGivenStopHClientFailsAndGReturnsNullValues() throws Exception {
    mockClientResponse(STOP_G_RESPONSE_MISSING_FIELDS, G_STOP_CODE);

    mockServer
        .expect(ExpectedCount.once(), requestTo(new URI(format(CLIENT_URL, H_STOP_CODE))))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

    mvc.perform(get(GET_API_URL))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    loadJsonFile(
                        "responses/getbustimes/get-bus-times-api-response-missing-values.json")));
  }

  private void mockClientResponse(String responsePath, String stopCode) throws IOException, URISyntaxException {
    mockServer
        .expect(ExpectedCount.once(), requestTo(new URI(format(CLIENT_URL, stopCode))))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(loadJsonFile(responsePath)));
  }
}
