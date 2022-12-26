package api.aggregator;

import lombok.Data;

@Data
public class RecommendationSummary {

	  private Long recommendationId;
	  private String author;
	  private Integer rate;
	  private String content;
	  
	  public RecommendationSummary() {}

	  public RecommendationSummary(Long recommendationId, String author, Integer rate, String content) {
	    this.recommendationId = recommendationId;
	    this.author = author;
	    this.rate = rate;
	    this.content=content;
	  }
}
