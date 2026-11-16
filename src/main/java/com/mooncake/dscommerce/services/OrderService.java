package com.mooncake.dscommerce.services;

import com.mooncake.dscommerce.dto.OrderDTO;
import com.mooncake.dscommerce.dto.OrderItemDTO;
import com.mooncake.dscommerce.entities.Order;
import com.mooncake.dscommerce.entities.OrderItem;
import com.mooncake.dscommerce.entities.Product;
import com.mooncake.dscommerce.entities.User;
import com.mooncake.dscommerce.entities.enums.OrderStatus;
import com.mooncake.dscommerce.mappers.OrderMapper;
import com.mooncake.dscommerce.repositories.OrderItemRepository;
import com.mooncake.dscommerce.repositories.OrderRepository;
import com.mooncake.dscommerce.repositories.ProductRepository;
import com.mooncake.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository repository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository,
            UserService userService,
            OrderMapper orderMapper) {

        this.repository = repository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado para o ID: " + id));
        return orderMapper.toDTO(entity);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {

        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated();

        order.setClient(user);

        for(OrderItemDTO itemDto: dto.getItems()){
            Product product = productRepository.getReferenceById(itemDto.getProductId());
            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return orderMapper.toDTO(order);
    }

}
