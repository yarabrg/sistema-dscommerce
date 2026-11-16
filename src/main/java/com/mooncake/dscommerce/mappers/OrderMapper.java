package com.mooncake.dscommerce.mappers;

import com.mooncake.dscommerce.dto.OrderDTO;
import com.mooncake.dscommerce.dto.PaymentDTO;
import com.mooncake.dscommerce.entities.Order;
import com.mooncake.dscommerce.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                OrderItemMapper.class
        }
)
public interface OrderMapper {

    OrderDTO toDTO(Order entity);

    PaymentDTO toPaymentDTO(Payment entity);
}