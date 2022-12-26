package api.aggregator;

import java.util.List;

import lombok.Data;

@Data
public class ProductAggregate {
	
	  private Long productId;
	  private String name;
	  private Integer weight;
	  private List<RecommendationSummary> recommendations;
	  private List<ReviewSummary> reviews;
	  private ServiceAddresses serviceAddresses;

	  public ProductAggregate(Long productId,String name,Integer weight,
			  List<RecommendationSummary> recommendations,List<ReviewSummary> reviews, 
			  ServiceAddresses serviceAddresses) {

	    this.productId = productId;
	    this.name = name;
	    this.weight = weight;
	    this.recommendations = recommendations;
	    this.reviews = reviews;
	    this.serviceAddresses = serviceAddresses;
	  }
}
