package main.services;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.core.recommendation.Recommendation;
import api.core.recommendation.RecommendationService;
import api.event.Event;
import api.exceptions.EventProcessingException;

@Configuration
public class MessageProcessorConfig{

  private final RecommendationService recommendationService;

  public MessageProcessorConfig(RecommendationService recommendationService) {
    this.recommendationService = recommendationService;
  }

  @Bean
  public Consumer<Event<Long, Recommendation>> messageProcessor() {
    return event -> {

      switch (event.getEventType()) {

        case CREATE:
          Recommendation recommendation = event.getData();
          recommendationService.createRecommendation(recommendation).block();
          break;

        case DELETE:
          Long productId = event.getKey();
          recommendationService.deleteRecommendations(productId).block();
          break;

        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          throw new EventProcessingException(errorMessage);
      }
    };
  }
}
