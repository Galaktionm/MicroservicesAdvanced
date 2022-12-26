package main.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Long> {
     List<ReviewEntity> findByProductId(Long productId);
}
