package main.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import api.core.review.Review;

@Component
public class ReviewMapper {
	
	public Review entityToApi(ReviewEntity entity) {	
		Review review=new Review(entity.getProductId(), entity.getReviewId(), entity.getAuthor(),
				entity.getSubject(), entity.getContent());
		return review;	
	}
	
	public ReviewEntity apiToEntity(Review review) {
		ReviewEntity entity = 
				new ReviewEntity(review.getProductId(), review.getReviewId(), 
						review.getAuthor(), review.getSubject(), review.getContent());
		return entity;		
	}
	
	 public List<Review> entityListToApiList(List<ReviewEntity> entityList){
		 List<Review> reviewList=new ArrayList<Review>();
		 for(ReviewEntity entity:entityList) {
			 reviewList.add(entityToApi(entity));
		 }
		 return reviewList;
	 }
	 
	 public List<ReviewEntity> apiListToEntityList(List<Review> reviewList){
		 List<ReviewEntity> entityList=new ArrayList<ReviewEntity>();
		 for(Review review:reviewList) {
			 entityList.add(apiToEntity(review));
		 }
		 return entityList;
	 }
 
}