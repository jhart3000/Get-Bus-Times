package com.bus.times.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@Builder
@JsonInclude(NON_NULL)
public class BusTimesResponse {
  List<BusTimeDetail> greatPortlandStreetStopG;
  List<BusTimeDetail> greatPortlandStreetStopH;
}
