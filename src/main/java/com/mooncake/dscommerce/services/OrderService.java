package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.OrderDTO;
import com.mooncake.dscommerce.entities.Order;
import com.mooncake.dscommerce.mappers.OrderMapper;
import com.mooncake.dscommerce.repositories.OrderRepository;
import com.mooncake.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository repository,
            OrderMapper orderMapper) {

        this.repository = repository;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado para o ID: " + id));
        return orderMapper.toDTO(entity);
    }
}
