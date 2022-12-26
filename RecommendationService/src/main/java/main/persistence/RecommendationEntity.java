package main.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "recommendations")
@CompoundIndex(name = "prod-rec-id", unique = true, def = "{'productId': 1, 'recommendationId' : 1}")
@Data
public class RecommendationEntity {

  @Id
  private String id;

  @Version
  private Integer version;

  private Long productId;
  private Long recommendationId;
  private String author;
  private Integer rating;
  private String content;

  public RecommendationEntity() {}

  public RecommendationEntity(Long productId, Long recommendationId, String author, Integer rating, String content) {
    this.productId = productId;
    this.recommendationId = recommendationId;
    this.author = author;
    this.rating = rating;
    this.content = content;
  }
  
}
