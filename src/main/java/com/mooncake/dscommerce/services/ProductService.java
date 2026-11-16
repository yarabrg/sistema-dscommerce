package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.CategoryDTO;
import com.mooncake.dscommerce.dto.ProductDTO;
import com.mooncake.dscommerce.dto.ProductMinDTO;
import com.mooncake.dscommerce.entities.Category;
import com.mooncake.dscommerce.entities.Product;
import com.mooncake.dscommerce.mappers.ProductMapper;
import com.mooncake.dscommerce.repositories.CategoryRepository;
import com.mooncake.dscommerce.repositories.ProductRepository;
import com.mooncake.dscommerce.services.exceptions.DatabaseException;
import com.mooncake.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado para o ID: " + id));
        return productMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> entity = productRepository.searchByName(name, pageable);
        return entity.map(productMapper::toMinDTO);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        Product entity = productMapper.toEntity(dto);

        Set<Category> categories = getCategories(dto);

        entity.getCategories().addAll(categories);
        entity = productRepository.save(entity);

        return productMapper.toDTO(entity);
    }

    @Transactional
    public ProductDTO update(ProductDTO dto, Long id) {

        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado para o ID: " + id));

        productMapper.updateEntity(dto, entity);

        Set<Category> categories = getCategories(dto);

        entity.getCategories().clear();
        entity.getCategories().addAll(categories);

        return productMapper.toDTO(entity);
    }

    @Transactional()
    public void delete(Long id){
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Produto não encontrado para o ID: " + id);
        }
        try {
            productRepository.deleteById(id);
            productRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    private Set<Category> getCategories(ProductDTO dto) {

        Set<Long> categoryIds = dto.getCategories().stream()
                .map(CategoryDTO::getId)
                .collect(Collectors.toSet());

        Set<Category> categories = new HashSet<>(
                categoryRepository.findAllById(categoryIds)
        );

        if (categories.size() != categoryIds.size()) {
            throw new ResourceNotFoundException(
                    "Uma ou mais categorias não foram encontradas");
        }

        return categories;
    }
}
