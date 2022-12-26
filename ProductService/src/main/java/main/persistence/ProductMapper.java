package main.persistence;

import org.springframework.stereotype.Component;

import api.core.product.Product;

@Component
public class ProductMapper {
	
   public Product entityToApi(ProductEntity entity) {
	   Product product=new Product(entity.getProductId(), entity.getName(), entity.getWeight());
	   return product;
   }
   
   public ProductEntity apiToEntity(Product product) {
	   ProductEntity entity=new ProductEntity(product.getProductId(), product.getName(), product.getWeight());
	   return entity;
   }

}
