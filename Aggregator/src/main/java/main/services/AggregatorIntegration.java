package main.services;



import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import api.core.product.Product;
import api.core.product.ProductService;
import api.core.recommendation.Recommendation;
import api.core.recommendation.RecommendationService;
import api.core.review.Review;
import api.core.review.ReviewService;
import api.event.Event;
import api.exceptions.InvalidInputException;
import api.exceptions.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import util.http.HttpErrorInfo;

public class AggregatorIntegration implements ProductService, RecommendationService, ReviewService {

	private Scheduler publishEventScheduler;
	
	private WebClient webClient;
	
	private ObjectMapper mapper;
	
	private StreamBridge streamBridge;
	
	 private final String productServiceUrl;
	 private final String recommendationServiceUrl;
	 private final String reviewServiceUrl;
	
	public AggregatorIntegration(Scheduler publishEventScheduler, WebClient.Builder webClientBuilder,
		    ObjectMapper mapper, StreamBridge streamBridge,
		    
		    @Value("${app.product-service.host}") String productServiceHost,
		    @Value("${app.product-service.port}") int  productServicePort,

		    @Value("${app.recommendation-service.host}") String recommendationServiceHost,
		    @Value("${app.recommendation-service.port}") int  recommendationServicePort,

		    @Value("${app.review-service.host}") String reviewServiceHost,
		    @Value("${app.review-service.port}") int  reviewServicePort) {
		
		this.publishEventScheduler=publishEventScheduler;
		this.webClient=webClientBuilder.build();
		this.mapper=mapper;
		this.streamBridge=streamBridge;
		
		productServiceUrl        = "http://" + productServiceHost + ":" + productServicePort;
	    recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort;
	    reviewServiceUrl         = "http://" + reviewServiceHost + ":" + reviewServicePort;
	}

	@Override
	public Mono<Review> createReview(Review body) {
		 return Mono.fromCallable(() -> {
		      sendMessage("reviews-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
		      return body;
		 }).subscribeOn(publishEventScheduler);
	}

	@Override
	public Flux<Review> getReviews(Long productId) {
		String url = reviewServiceUrl + "/review?productId=" + productId;
	    // Return an empty result if something goes wrong to make it possible for the composite service to return partial responses
	    return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).onErrorResume(error -> Flux.empty());
	}

	@Override
	public Mono<Void> deleteReviews(Long productId) {
		  return Mono.fromRunnable(() -> sendMessage("reviews-out-0", new Event(Event.Type.DELETE, productId, null)))
			      .subscribeOn(publishEventScheduler).then();
	}

	@Override
	public Mono<Recommendation> createRecommendation(Recommendation body) {
		 return Mono.fromCallable(() -> {
		      sendMessage("recommendations-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
		      return body;
		    }).subscribeOn(publishEventScheduler);
	}

	@Override
	public Flux<Recommendation> getRecommendations(Long productId) {
		 String url = recommendationServiceUrl + "/recommendation?productId=" + productId;
		 return webClient.get().uri(url).retrieve().bodyToFlux(Recommendation.class).onErrorResume(error -> Flux.empty());
	}

	@Override
	public Mono<Void> deleteRecommendations(Long productId) {
		return Mono.fromRunnable(() -> sendMessage("recommendations-out-0", new Event(Event.Type.DELETE, productId, null)))
			      .subscribeOn(publishEventScheduler).then();
	}

	@Override
	public Mono<Product> createProduct(Product body) {
		 return Mono.fromCallable(() -> {
		      sendMessage("products-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
		      return body;
		    }).subscribeOn(publishEventScheduler);
	}

	@Override
	public Mono<Product> getProduct(Long productId) {
		 String url = productServiceUrl + "/product/" + productId;
         return webClient.get().uri(url).retrieve().bodyToMono(Product.class).onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
	}

	@Override
	public Mono<Void> deleteProduct(Long productId) {
		 return Mono.fromRunnable(() -> sendMessage("products-out-0", new Event(Event.Type.DELETE, productId, null)))
			      .subscribeOn(publishEventScheduler).then();
	}
	
	 private void sendMessage(String bindingName, Event event) {
		    Message message = MessageBuilder.withPayload(event)
		      .setHeader("partitionKey", event.getKey())
		      .build();
		    streamBridge.send(bindingName, message);
	}
	 
	 private Throwable handleException(Throwable ex) {

		    if (!(ex instanceof WebClientResponseException)) {		     
		      return ex;
		    }

		    WebClientResponseException wcre = (WebClientResponseException)ex;

		    switch (wcre.getStatusCode().value()) {

		      //Not Found
		      case 404:
		        return new NotFoundException(getErrorMessage(wcre));
              //Unprocessable Entity
		      case 422 :
		        return new InvalidInputException(getErrorMessage(wcre));

		      default:		       
		        return ex;
		    }
		  }
	 
	 private String getErrorMessage(WebClientResponseException ex) {
		    try {
		      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
		    } catch (IOException ioex) {
		      return ex.getMessage();
		    }
	}


}
