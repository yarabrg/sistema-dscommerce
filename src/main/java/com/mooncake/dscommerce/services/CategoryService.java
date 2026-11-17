package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.CategoryDTO;
import com.mooncake.dscommerce.entities.Category;
import com.mooncake.dscommerce.mappers.CategoryMapper;
import com.mooncake.dscommerce.repositories.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryRepository repository,
            CategoryMapper categoryMapper) {

        this.repository = repository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categories = repository.findAll();
        return categories.stream().map(categoryMapper::toDTO).toList();
    }

}
