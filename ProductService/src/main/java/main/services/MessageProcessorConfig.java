package main.services;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import api.core.product.Product;
import api.core.product.ProductService;
import api.event.Event;
import api.exceptions.EventProcessingException;

@Configuration
public class MessageProcessorConfig {

  private final ProductService productService;

  public MessageProcessorConfig(ProductService productService) {
    this.productService = productService;
  }

  @Bean
  public Consumer<Event<Long, Product>> messageProcessor() {
    return event -> {

      switch (event.getEventType()) {

        case CREATE:
          Product product = event.getData();
          productService.createProduct(product).block();
          break;

        case DELETE:
          Long productId = event.getKey();
          productService.deleteProduct(productId).block();
          break;

        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          throw new EventProcessingException(errorMessage);
      }

    };
  }
}