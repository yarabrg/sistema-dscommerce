package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.ProductDTO;
import com.mooncake.dscommerce.entities.Product;
import com.mooncake.dscommerce.repositories.ProductRepository;
import com.mooncake.dscommerce.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository repository, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado para o ID: " + id));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> product = repository.findAll(pageable);
        return product.map(x -> modelMapper.map(x, ProductDTO.class));
    }
}
