package com.mooncake.dscommerce.mappers;

import com.mooncake.dscommerce.dto.OrderItemDTO;
import com.mooncake.dscommerce.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    OrderItemDTO toDTO(OrderItem entity);
}