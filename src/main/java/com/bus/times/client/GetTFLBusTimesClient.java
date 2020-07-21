package com.bus.times.client;

import com.bus.times.model.client.TFLClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class GetTFLBusTimesClient {

  @Autowired private RestTemplate restTemplate;

  public List<TFLClientResponse> getListOfBusTimesForStop(String stopCode) {
    try {
      String uri = format("https://api.tfl.gov.uk/StopPoint/%s/Arrivals", stopCode);
      return Arrays.asList(
          Objects.requireNonNull(restTemplate.getForObject(uri, TFLClientResponse[].class)));
    } catch (RuntimeException e) {
      return List.of();
    }
  }
}
