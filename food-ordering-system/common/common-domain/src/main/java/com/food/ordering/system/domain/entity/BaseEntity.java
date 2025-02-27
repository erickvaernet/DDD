package com.food.ordering.system.domain.entity;

import java.util.Objects;

public abstract class BaseEntity<ID> {

    private ID OrderId;

    public ID getId() {
        return OrderId;
    }

    public void setId(ID id) {
        this.OrderId = id;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(OrderId, that.OrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(OrderId);
    }
}
