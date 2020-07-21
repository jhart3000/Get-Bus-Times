package com.bus.times.configuration;

import com.bus.times.client.GetTFLBusTimesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GetTFLBustTimesClientConfiguration {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public GetTFLBusTimesClient getTFLBusTimesClient() {
    return new GetTFLBusTimesClient();
  }
}
