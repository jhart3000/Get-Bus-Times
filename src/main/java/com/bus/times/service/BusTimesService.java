package com.bus.times.service;

import com.bus.times.client.GetTFLBusTimesClient;
import com.bus.times.exception.InternalException;
import com.bus.times.model.client.TFLClientResponse;
import com.bus.times.model.domain.BusTimeDetail;
import com.bus.times.model.domain.BusTimesResponse;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class BusTimesService {

  private GetTFLBusTimesClient client;

  public BusTimesService(GetTFLBusTimesClient client) {
    this.client = client;
  }

  public BusTimesResponse getBusTimesForStopsGandH() throws InternalException {

    List<BusTimeDetail> stopGList =
        convertToSortedBusTimeDetailList(client.getListOfBusTimesForStop("490000091G"));
    List<BusTimeDetail> stopHList =
        convertToSortedBusTimeDetailList(client.getListOfBusTimesForStop("490000091H"));

    return BusTimesResponse.builder()
        .greatPortlandStreetStopH(stopHList)
        .greatPortlandStreetStopG(stopGList)
        .build();
  }

  private List<BusTimeDetail> convertToSortedBusTimeDetailList(List<TFLClientResponse> clientList)
      throws InternalException {
    try {
      return clientList.stream()
          .sorted(Comparator.comparing(TFLClientResponse::getExpectedArrival))
          .map(this::buildBusTimeDetail)
          .collect(Collectors.toList());

    } catch (Exception e) {
      throw new InternalException(
          format("Invalid expected arrival retrieved from tfl client: %s", e.getMessage()));
    }
  }

  private BusTimeDetail buildBusTimeDetail(TFLClientResponse busDetail) {
    return BusTimeDetail.builder()
        .busNumber(busDetail.getLineId())
        .destination(busDetail.getDestinationName())
        .expectedArrival(getReadableDate(busDetail.getExpectedArrival()))
        .arrivalTimeInMinutes(TimeUnit.SECONDS.toMinutes(busDetail.getTimeToStation()))
        .build();
  }

  private String getReadableDate(String arrivalDateTime) {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    ZonedDateTime zonedDateTime =
        ZonedDateTime.parse(arrivalDateTime, inputFormatter.withZone(ZoneId.of("UTC")))
            .withZoneSameInstant(ZoneId.of("Europe/London"));
    return DateTimeFormatter.ofPattern("EEE dd MMM yyyy hh:mm a").format(zonedDateTime);
  }
}
