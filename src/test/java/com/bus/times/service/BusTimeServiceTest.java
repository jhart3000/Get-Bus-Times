package com.bus.times.service;

import com.bus.times.client.GetTFLBusTimesClient;
import com.bus.times.exception.InternalException;
import com.bus.times.model.client.TFLClientResponse;
import com.bus.times.model.domain.BusTimeDetail;
import com.bus.times.model.domain.BusTimesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.bus.times.helper.TestDataHelper.G_STOP_CODE;
import static com.bus.times.helper.TestDataHelper.H_STOP_CODE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class BusTimeServiceTest {

  @Autowired private BusTimesService service;

  @MockBean private GetTFLBusTimesClient client;

  private static final String BUS_NUMBER = "30";
  private static final String DESTINATION = "Marble Arch";
  private static final String EXPECTED_ARRIVAL = "2020-07-13T13:57:05Z";

  @Test
  void shouldBuildResponseObjectFromClientObject() throws Exception {
    mockClientCall(G_STOP_CODE, DESTINATION, BUS_NUMBER, EXPECTED_ARRIVAL);
    mockClientCall(H_STOP_CODE, DESTINATION, BUS_NUMBER, EXPECTED_ARRIVAL);

    BusTimesResponse response = service.getBusTimesForStopsGandH();

    assertThat(response).isNotNull();
    assertThat(response.getGreatPortlandStreetStopG()).isNotEmpty();
    assertThat(response.getGreatPortlandStreetStopH()).isNotEmpty();
    assertThat(response.getGreatPortlandStreetStopH())
        .extracting(BusTimeDetail::getBusNumber, BusTimeDetail::getDestination)
        .contains(tuple(BUS_NUMBER, DESTINATION));
    assertThat(response.getGreatPortlandStreetStopG())
        .extracting(BusTimeDetail::getBusNumber, BusTimeDetail::getDestination)
        .contains(tuple(BUS_NUMBER, DESTINATION));
  }

  @Test
  void shouldBuildResponseObjectFromClientObjectWithNullFields() throws Exception {
    mockClientCall(G_STOP_CODE, null, null, EXPECTED_ARRIVAL);
    given(client.getListOfBusTimesForStop(H_STOP_CODE)).willReturn(List.of());

    BusTimesResponse response = service.getBusTimesForStopsGandH();

    assertThat(response).isNotNull();
    assertThat(response.getGreatPortlandStreetStopG()).isNotEmpty();
    assertThat(response.getGreatPortlandStreetStopH()).isEmpty();
    assertThat(response.getGreatPortlandStreetStopG())
        .extracting(BusTimeDetail::getBusNumber, BusTimeDetail::getDestination)
        .contains(tuple(null, null));
  }

  @Test
  void shouldThrowInternalErrorDueToInvalidExpectedArrivalFormat() {
    mockClientCall(G_STOP_CODE, null, null, "2020-07-13T13:57Z");
    mockClientCall(H_STOP_CODE, null, null, EXPECTED_ARRIVAL);

    Throwable errorResponse = catchThrowable(() -> service.getBusTimesForStopsGandH());
    assertThat(errorResponse).isInstanceOf(InternalException.class);
  }

  @Test
  void shouldThrowInternalErrorDueToNullExpectedArrival() {
    mockClientCall(G_STOP_CODE, null, null, null);
    mockClientCall(H_STOP_CODE, null, null, null);

    Throwable errorResponse = catchThrowable(() -> service.getBusTimesForStopsGandH());
    assertThat(errorResponse).isInstanceOf(InternalException.class);
  }

  private static TFLClientResponse buildClientResponseList(
      String destination, String lineId, String expectedArrival) {
    return TFLClientResponse.builder()
        .destinationName(destination)
        .expectedArrival(expectedArrival)
        .lineId(lineId)
        .timeToStation(1639)
        .build();
  }

  private void mockClientCall(
      String stopCode, String destination, String lineId, String expectedArrival) {
    given(client.getListOfBusTimesForStop(stopCode))
        .willReturn(List.of(buildClientResponseList(destination, lineId, expectedArrival)));
  }
}
