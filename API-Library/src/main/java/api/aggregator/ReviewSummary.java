package api.aggregator;

import lombok.Data;

@Data
public class ReviewSummary {

	  private Long reviewId;
	  private String author;
	  private String subject;
	  private String content;
	  
	  public ReviewSummary() {}

	  public ReviewSummary(Long reviewId, String author, String subject, String content) {
	    this.reviewId = reviewId;
	    this.author = author;
	    this.subject = subject;
	    this.content=content;
	  }
}