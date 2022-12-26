package main.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;

@Document(collection = "products")
@Data
public class ProductEntity {

  @Id 
  private String id;

  @Version 
  private Integer version;

  @Indexed(unique = true)
  private Long productId;

  private String name;
  private Integer weight;

  public ProductEntity() {}

  public ProductEntity(Long productId, String name, Integer weight) {
    this.productId = productId;
    this.name = name;
    this.weight = weight;
  }
}