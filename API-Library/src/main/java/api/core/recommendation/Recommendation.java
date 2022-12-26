package api.core.recommendation;

import lombok.Data;

@Data
public class Recommendation {
	 
	  private Long productId;
	  private Long recommendationId;
	  private String author;
	  private Integer rate;
	  private String content;
	  private String serviceAddress="";

	  public Recommendation() {}
	  
	  public Recommendation(Long productId, Long recommendationId, String author,
			  Integer rate, String content) {
	    this.productId = productId;
	    this.recommendationId = recommendationId;
	    this.author = author;
	    this.rate = rate;
	    this.content = content;
	  }

	  public Recommendation(Long productId, Long recommendationId, String author,
			  Integer rate, String content, String serviceAddress) {
	    this.productId = productId;
	    this.recommendationId = recommendationId;
	    this.author = author;
	    this.rate = rate;
	    this.content = content;
	    this.serviceAddress = serviceAddress;
	  }
	  
}