package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import api.core.product.Product;
import main.persistence.ProductEntity;
import main.persistence.ProductRepository;
import reactor.core.publisher.Mono;
import util.http.ServiceUtil;

@SpringBootApplication
@ComponentScan(basePackages= {"api.core.product", "api.exceptions", "util.http", "main"})
@EnableReactiveMongoRepositories
public class ProductServiceApplication {
	
	@Autowired
	private ServiceUtil serviceUtil;
	@Autowired
	private ProductRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

/*
	@Bean
	CommandLineRunner loadData() {
		return args->{
			ProductEntity product=new ProductEntity(1L, "Somehow I Manage", 3);
			Mono<ProductEntity> result=repo.save(product);	
			int x=0;
		};
	}
*/

}
