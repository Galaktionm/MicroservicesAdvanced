package main.controllers;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import api.core.review.Review;
import api.core.review.ReviewService;
import api.exceptions.InvalidInputException;
import main.persistence.ReviewEntity;
import main.persistence.ReviewMapper;
import main.persistence.ReviewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import util.http.ServiceUtil;

@RestController
public class ReviewServiceImpl implements ReviewService {
	
	  private Scheduler jdbcScheduler;
	
	  private ReviewRepository repository;

	  private ReviewMapper mapper;

	  private ServiceUtil serviceUtil;
	
	public ReviewServiceImpl(Scheduler jdbcScheduler, ReviewRepository repository, 
			ReviewMapper mapper, ServiceUtil serviceUtil) {
		this.jdbcScheduler=jdbcScheduler;
		this.repository=repository;
		this.mapper=mapper;
		this.serviceUtil=serviceUtil;
	}

	@Override
	public Mono<Review> createReview(Review body) {
		if (body.getProductId() < 1) {
		      throw new InvalidInputException("Invalid productId: " + body.getProductId());
		    }
		    return Mono.fromCallable(() -> internalCreateReview(body))
		      .subscribeOn(jdbcScheduler);
	}
	
	private Review internalCreateReview(Review body) {
	    try {
	      ReviewEntity entity = mapper.apiToEntity(body);
	      ReviewEntity newEntity = repository.save(entity);

	      return mapper.entityToApi(newEntity);

	    } catch (DataIntegrityViolationException dive) {
	      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
	    }
	  }


	@Override
	public Flux<Review> getReviews(Long productId) {
		  if (productId < 1) {
		      throw new InvalidInputException("Invalid productId: " + productId);
		   }
		    
		  return Mono.fromCallable(() -> internalGetReviews(productId))
		      .flatMapMany(Flux::fromIterable)
		      .subscribeOn(jdbcScheduler);
	}
	
	private List<Review> internalGetReviews(Long productId) {

	    List<ReviewEntity> entityList = repository.findByProductId(productId);
	    List<Review> list = mapper.entityListToApiList(entityList);
	    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

	    return list;
	  }

	@Override
	public Mono<Void> deleteReviews(Long productId) {
		 if (productId < 1) {
		      throw new InvalidInputException("Invalid productId: " + productId);
		 }

		return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
	}
	
	 private void internalDeleteReviews(Long productId) {		    
		 repository.deleteAll(repository.findByProductId(productId));
	}



}
