package com.bus.times.controller;

import com.bus.times.exception.InternalException;
import com.bus.times.model.domain.BusTimesResponse;
import com.bus.times.service.BusTimesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetBusTimesController {

  @Autowired private BusTimesService service;

  @GetMapping("/greatPortlandStreetTimes")
  @Operation(
      summary =
          "returns the expected times of each bus at stop G and H at Great Portland StreetStation",
      description =
          "This api will call the tfl unified API two times each with a different stop point id. "
              + "One for stop G and one for stop H. "
              + "It will then convert the data into a more readable format for the user and return it in this response")
  public BusTimesResponse getBusTimes() throws InternalException {
    return service.getBusTimesForStopsGandH();
  }
}
