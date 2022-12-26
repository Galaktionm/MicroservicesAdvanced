package api.core.review;

import lombok.Data;

@Data
public class Review {
	 
	  private Long productId;
	  private Long reviewId;
	  private String author;
	  private String subject;
	  private String content;
	  private String serviceAddress;

	  public Review() {}
	  
	  public Review(Long productId, Long reviewId, String author,  String subject, String content) {
			    
		  this.productId = productId;
		  this.reviewId = reviewId;
		  this.author = author;
		  this.subject = subject;
		  this.content = content;
	  }

	  public Review(Long productId, Long reviewId, String author,
	    String subject, String content, String serviceAddress) {

	    this.productId = productId;
	    this.reviewId = reviewId;
	    this.author = author;
	    this.subject = subject;
	    this.content = content;
	    this.serviceAddress = serviceAddress;
	  }
}