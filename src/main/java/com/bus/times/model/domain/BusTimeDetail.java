package com.bus.times.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@Builder
@JsonInclude(NON_NULL)
public class BusTimeDetail {
  String busNumber;
  String destination;
  String expectedArrival;
  long arrivalTimeInMinutes;
}
