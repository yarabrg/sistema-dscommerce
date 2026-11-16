package com.mooncake.dscommerce.mappers;

import com.mooncake.dscommerce.dto.CategoryDTO;
import com.mooncake.dscommerce.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category entity);

    Category toEntity(CategoryDTO dto);
}