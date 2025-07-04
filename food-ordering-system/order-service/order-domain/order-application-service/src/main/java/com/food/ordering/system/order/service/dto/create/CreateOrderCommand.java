package com.food.ordering.system.order.service.dto.create;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {

    private final UUID customerId;
    private final UUID restaurantId;
    private final Money price;
    private final BigDecimal orderId;
}
