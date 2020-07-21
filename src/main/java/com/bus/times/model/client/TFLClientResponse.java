package com.bus.times.model.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = TFLClientResponse.Builder.class)
public class TFLClientResponse {

  String lineId;
  String destinationName;
  long timeToStation;
  String expectedArrival;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {}
}
