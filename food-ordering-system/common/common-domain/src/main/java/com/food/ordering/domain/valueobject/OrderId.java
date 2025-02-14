package com.food.ordering.domain.valueobject;

import com.food.ordering.domain.entity.BaseEntity;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {

    public OrderId(UUID value) {
        super(value);
    }
}
