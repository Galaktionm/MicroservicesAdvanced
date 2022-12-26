package main.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import api.core.recommendation.Recommendation;
import main.persistence.RecommendationEntity;

@Component
public class RecommendationMapper {
  
  public Recommendation entityToApi(RecommendationEntity entity) {
	  Recommendation rec=new Recommendation(entity.getProductId(), entity.getRecommendationId(), 
			  entity.getAuthor(), entity.getRating(), entity.getContent());
	  return rec;
  }
  
  public RecommendationEntity apiToEntity(Recommendation recommendation) {
	  RecommendationEntity entity=new RecommendationEntity(recommendation.getProductId(), 
			  recommendation.getRecommendationId(), recommendation.getAuthor(), recommendation.getRate(), 
			  recommendation.getContent());
	  return entity;
  }
  
  public List<Recommendation> entityListToApiList(List<RecommendationEntity> entityList){
	  List<Recommendation> list=new ArrayList<Recommendation>();
	  for(RecommendationEntity entity:entityList){
		  list.add(entityToApi(entity));
	  }
	  return list;
  }
  
  public List<RecommendationEntity> apiToEntityList(List<Recommendation> recommendationList){
	  List<RecommendationEntity> entityList=new ArrayList<RecommendationEntity>();
	  for(Recommendation recommendation:recommendationList) {
		  entityList.add(apiToEntity(recommendation));
	  }
	  return entityList;
  }  
  
}