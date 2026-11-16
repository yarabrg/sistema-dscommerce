package com.mooncake.dscommerce.repositories;

import com.mooncake.dscommerce.entities.Order;
import com.mooncake.dscommerce.entities.OrderItem;
import com.mooncake.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
