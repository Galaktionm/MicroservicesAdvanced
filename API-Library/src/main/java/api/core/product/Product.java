package api.core.product;

import lombok.Data;

@Data
public class Product {
 
  private Long productId;
  private String name;
  private Integer weight;
  private String serviceAddress="";

  public Product() {}
  
  public Product(Long productId, String name, Integer weight) {
	    this.productId = productId;
	    this.name = name;
	    this.weight = weight;
	  }

  public Product(Long productId, String name, Integer weight, String serviceAddress) {
    this.productId = productId;
    this.name = name;
    this.weight = weight;
    this.serviceAddress = serviceAddress;
  }
}