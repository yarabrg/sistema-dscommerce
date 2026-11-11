package com.mooncake.dscommerce.repositories;

import com.mooncake.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> id(Long id);
}
