package sboot.service.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sboot.service.order.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
