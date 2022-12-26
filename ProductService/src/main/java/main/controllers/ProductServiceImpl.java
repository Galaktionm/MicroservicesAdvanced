package main.controllers;

import com.mongodb.DuplicateKeyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import api.core.product.Product;
import api.core.product.ProductService;
import api.exceptions.InvalidInputException;
import api.exceptions.NotFoundException;
import main.persistence.ProductEntity;
import main.persistence.ProductMapper;
import main.persistence.ProductRepository;
import reactor.core.publisher.Mono;
import util.http.ServiceUtil;

@RestController
public class ProductServiceImpl implements ProductService {
	 
	  private ServiceUtil serviceUtil;

	  private ProductRepository repository;

	  private ProductMapper mapper;

	  public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil) {
	    this.repository = repository;
	    this.mapper = mapper;
	    this.serviceUtil = serviceUtil;
	  }

	  @Override
	  public Mono<Product> createProduct(Product body) {
		  if (body.getProductId() < 1) {
		      throw new InvalidInputException("Invalid productId: " + body.getProductId());
		    }
	    
	      ProductEntity entity = mapper.apiToEntity(body);
	      Mono<Product> newEntity = repository.save(entity)
	    		  .onErrorMap(DuplicateKeyException.class, 
	    	ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
	    		  .map(e->mapper.entityToApi(e));	    	
	      return newEntity;    
	  }

	  @Override
	  public Mono<Product> getProduct(Long productId) {

	    if (productId < 1) {
	      throw new InvalidInputException("Invalid productId: " + productId);
	    }
	    
	    Mono<ProductEntity> entity=repository.findByProductId(productId);

	    return entity
	      .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
	      .map(e -> mapper.entityToApi(e))
	      .map(e -> setServiceAddress(e));
	  }

	  @Override
	  public Mono<Void> deleteProduct(Long productId) {		  
		  if (productId < 1) {
		      throw new InvalidInputException("Invalid productId: " + productId);
		    }	
		 return repository.findByProductId(productId).map(e -> repository.delete(e)).flatMap(e -> e);		      
	  }
	  
	  private Product setServiceAddress(Product e) {
		    e.setServiceAddress(serviceUtil.getServiceAddress());
		    return e;
	  }

}
