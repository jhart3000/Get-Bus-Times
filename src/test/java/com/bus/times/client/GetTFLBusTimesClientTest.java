package com.bus.times.client;

import com.bus.times.model.client.TFLClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.bus.times.helper.JsonLoader.loadJsonFile;
import static com.bus.times.helper.TestDataHelper.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetTFLBusTimesClientTest {

  @Autowired private RestTemplate restTemplate;

  @Autowired private GetTFLBusTimesClient client;

  private MockRestServiceServer mockServer;

  private static final String DATE = "2020-07-13T13:54:18Z";
  private static final String DESTINATION = "Sudbury";
  private static final String BUS_NUMBER = "18";
  private static final long SECONDS_TO_ARRIVAL = 1772L;

  @BeforeEach
  void init() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void shouldReturnSuccessfulClientResponseGivenValidStopCode()
      throws URISyntaxException, IOException {
    mockServer
        .expect(ExpectedCount.once(), requestTo(new URI(format(CLIENT_URL, G_STOP_CODE))))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(loadJsonFile(STOP_G_CLIENT_RESPONSE_PATH)));

    List<TFLClientResponse> response = client.getListOfBusTimesForStop(G_STOP_CODE);

    assertThat(response).isNotEmpty();
    assertThat(response)
        .extracting(
            TFLClientResponse::getExpectedArrival,
            TFLClientResponse::getLineId,
            TFLClientResponse::getTimeToStation,
            TFLClientResponse::getDestinationName)
        .contains(tuple("2020-07-13T13:59:18Z", BUS_NUMBER, SECONDS_TO_ARRIVAL, DESTINATION));
  }

  @Test
  void shouldReturnSuccessfulClientResponseGivenMissingFields()
      throws URISyntaxException, IOException {
    mockServer
        .expect(ExpectedCount.once(), requestTo(new URI(format(CLIENT_URL, G_STOP_CODE))))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(loadJsonFile(STOP_G_RESPONSE_MISSING_FIELDS)));

    List<TFLClientResponse> response = client.getListOfBusTimesForStop(G_STOP_CODE);

    assertThat(response).isNotEmpty();
    assertThat(response)
        .extracting(
            TFLClientResponse::getExpectedArrival,
            TFLClientResponse::getLineId,
            TFLClientResponse::getTimeToStation,
            TFLClientResponse::getDestinationName)
        .contains(
            tuple(DATE, null, SECONDS_TO_ARRIVAL, DESTINATION), tuple(DATE, BUS_NUMBER, 0L, null));
  }

  @Test
  void shouldReturnEmptyListGivenClientThrowsException() throws URISyntaxException {
    mockServer
        .expect(ExpectedCount.once(), requestTo(new URI(format(CLIENT_URL, G_STOP_CODE))))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());
    List<TFLClientResponse> response = client.getListOfBusTimesForStop(G_STOP_CODE);
    assertThat(response).isEmpty();
  }
}
