package com.bus.times.configuration;

import com.bus.times.client.GetTFLBusTimesClient;
import com.bus.times.service.BusTimesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusTimesServiceConfiguration {

  @Bean
  BusTimesService busTimesService(GetTFLBusTimesClient getTFLBusTimesClient) {
    return new BusTimesService(getTFLBusTimesClient);
  }
}
