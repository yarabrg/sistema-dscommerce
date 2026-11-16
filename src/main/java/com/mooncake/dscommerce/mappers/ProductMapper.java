package com.mooncake.dscommerce.mappers;

import com.mooncake.dscommerce.dto.ProductDTO;
import com.mooncake.dscommerce.dto.ProductMinDTO;
import com.mooncake.dscommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = CategoryMapper.class
)
public interface ProductMapper {

    ProductDTO toDTO(Product entity);

    @Mapping(target = "categories", ignore = true)
    Product toEntity(ProductDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateEntity(ProductDTO dto, @MappingTarget Product entity);

    ProductMinDTO toMinDTO(Product entity);
}