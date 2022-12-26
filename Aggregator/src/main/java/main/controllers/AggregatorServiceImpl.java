package main.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import api.aggregator.AggregatorService;
import api.aggregator.ProductAggregate;
import api.aggregator.RecommendationSummary;
import api.aggregator.ReviewSummary;
import api.aggregator.ServiceAddresses;
import api.core.product.Product;
import api.core.recommendation.Recommendation;
import api.core.review.Review;
import main.services.AggregatorIntegration;
import reactor.core.publisher.Mono;
import util.http.ServiceUtil;

public class AggregatorServiceImpl implements AggregatorService {
	
	private ServiceUtil serviceUtil;
	private AggregatorIntegration integration;
	
	public AggregatorServiceImpl(ServiceUtil serviceUtil, AggregatorIntegration integration) {
		this.serviceUtil=serviceUtil;
		this.integration=integration;
	}

	@Override
	public Mono<Void> createProduct(ProductAggregate body) {
		try {

		      List<Mono> monoList = new ArrayList<>();

		      Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
		      monoList.add(integration.createProduct(product));

		      if (body.getRecommendations() != null) {
		        body.getRecommendations().forEach(r -> {
		          Recommendation recommendation = new Recommendation(body.getProductId(), r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent());
		          monoList.add(integration.createRecommendation(recommendation));
		        });
		      }

		      if (body.getReviews() != null) {
		        body.getReviews().forEach(r -> {
		          Review review = new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent());
		          monoList.add(integration.createReview(review));
		        });
		      }

		      return Mono.zip(r -> "", monoList.toArray(new Mono[0])).then();

		    } catch (RuntimeException re) {		    
		      throw re;
		    }
		  }

		  @Override
		  public Mono<ProductAggregate> getProduct(Long productId) {

		    return Mono.zip(
		      values -> createProductAggregate((Product) values[0], (List<Recommendation>) values[1], (List<Review>) values[2], serviceUtil.getServiceAddress()),
		      integration.getProduct(productId),
		      integration.getRecommendations(productId).collectList(),
		      integration.getReviews(productId).collectList());
	}

	@Override
	public Mono<Void> deleteProduct(Long productId) {
		  try {
		      return Mono.zip(
		        r -> "",
		        integration.deleteProduct(productId),
		        integration.deleteRecommendations(productId),
		        integration.deleteReviews(productId)).then();	     

		    } catch (RuntimeException re) {		
		      throw re;
		    }
	}
	
	private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) {

	    Long productId = product.getProductId();
	    String name = product.getName();
	    Integer weight = product.getWeight();


	    List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null :
	       recommendations.stream()
	        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
	        .collect(Collectors.toList());


	    List<ReviewSummary> reviewSummaries = (reviews == null)  ? null :
	      reviews.stream()
	        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
	        .collect(Collectors.toList());


	    String productAddress = product.getServiceAddress();
	    String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
	    String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
	    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

	    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
	  }

	
}
