package main.controllers;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DuplicateKeyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import api.core.recommendation.Recommendation;
import api.core.recommendation.RecommendationService;
import api.exceptions.InvalidInputException;
import main.persistence.RecommendationEntity;
import main.persistence.RecommendationMapper;
import main.persistence.RecommendationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import util.http.ServiceUtil;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

	  private RecommendationRepository repository;
	  
	  private RecommendationMapper mapper;
	  
	  private ServiceUtil serviceUtil;
	  
	  

	  public RecommendationServiceImpl(RecommendationRepository repository, 
			  RecommendationMapper mapper, ServiceUtil serviceUtil) {
	    this.repository=repository;
	    this.mapper=mapper;
	    this.serviceUtil=serviceUtil;	    
	  }

	@Override
	public Flux<Recommendation> getRecommendations(Long productId) {
		 if (productId < 1) {
		      throw new InvalidInputException("Invalid productId: " + productId);
		 } 

		    return repository.findByProductId(productId)
		      .map(e -> mapper.entityToApi(e))
		      .map(e -> setServiceAddress(e));
	}

	@Override
	public Mono<Recommendation> createRecommendation(Recommendation body) {
		if (body.getProductId() < 1) {
		      throw new InvalidInputException("Invalid productId: " + body.getProductId());
		}
	    RecommendationEntity entity = mapper.apiToEntity(body);
		Mono<Recommendation> newEntity = repository.save(entity)		  
		      .onErrorMap(
		        DuplicateKeyException.class,
		        ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Recommendation Id:" + body.getRecommendationId()))
		      .map(e -> mapper.entityToApi(e));

		return newEntity;
	}
	

	@Override
	public Mono<Void> deleteRecommendations(Long productId) {

	    if (productId < 1) {
	      throw new InvalidInputException("Invalid productId: " + productId);
	    }

	    return repository.deleteAll(repository.findByProductId(productId));
	  }
	
	 private Recommendation setServiceAddress(Recommendation e) {
		    e.setServiceAddress(serviceUtil.getServiceAddress());
		    return e;
	  }

}
