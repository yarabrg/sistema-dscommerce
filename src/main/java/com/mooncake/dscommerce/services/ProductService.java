package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.ProductDTO;
import com.mooncake.dscommerce.entities.Product;
import com.mooncake.dscommerce.repositories.ProductRepository;
import com.mooncake.dscommerce.services.exceptions.DatabaseException;
import com.mooncake.dscommerce.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado para o ID: " + id));
        return modelMapper.map(entity, ProductDTO.class);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> entity = repository.findAll(pageable);
        return entity.map(x -> modelMapper.map(x, ProductDTO.class));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = repository.save(modelMapper.map(dto, Product.class));
        return modelMapper.map(entity, ProductDTO.class);
    }

    @Transactional
    public ProductDTO update(ProductDTO dto, Long id) {

        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado para o ID: " + id));
        modelMapper.typeMap(ProductDTO.class, Product.class)
                .addMappings(mapper -> mapper.skip(Product::setId));
        modelMapper.map(dto, entity);
        return modelMapper.map(entity, ProductDTO.class);
    }

    @Transactional()
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Produto não encontrado para o ID: " + id);
        }
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }
}
