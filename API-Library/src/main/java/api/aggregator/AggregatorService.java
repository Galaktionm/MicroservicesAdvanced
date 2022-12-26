package api.aggregator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import reactor.core.publisher.Mono;

public interface AggregatorService {
	
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/aggregator", consumes = "application/json")
    Mono<Void> createProduct(@RequestBody ProductAggregate body);


    @GetMapping(value = "/aggregator/{productId}",produces = "application/json")
    Mono<ProductAggregate> getProduct(@PathVariable Long productId);
    
    
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/aggregator/{productId}")
    Mono<Void> deleteProduct(@PathVariable Long productId);

    
}