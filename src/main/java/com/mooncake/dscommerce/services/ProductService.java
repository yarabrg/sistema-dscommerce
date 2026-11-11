package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.ProductDTO;
import com.mooncake.dscommerce.entities.Product;
import com.mooncake.dscommerce.repositories.ProductRepository;
import com.mooncake.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + id));
        return new ProductDTO(product);
    }
}
