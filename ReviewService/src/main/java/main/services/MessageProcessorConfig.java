package main.services;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.core.review.Review;
import api.core.review.ReviewService;
import api.event.Event;
import api.exceptions.EventProcessingException;

@Configuration
public class MessageProcessorConfig {

  private final ReviewService reviewService;

  public MessageProcessorConfig(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @Bean
  public Consumer<Event<Long, Review>> messageProcessor() {
    return event -> {

      switch (event.getEventType()) {

        case CREATE:
          Review review = event.getData();
          reviewService.createReview(review).block();
          break;

        case DELETE:
          Long productId = event.getKey();
          reviewService.deleteReviews(productId).block();
          break;

        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          throw new EventProcessingException(errorMessage);
      }
    };
  }
}
