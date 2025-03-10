package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.valueobject.OrderStatus.*;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = PENDING;
        initializeOrderItems();

    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay(){
        if (!orderStatus.equals(PENDING)){
            throw new OrderDomainException("Order is not in correct state for pay operataion");
        }
        orderStatus = PAID;
    }

    public  void approve(){
        if (!orderStatus.equals(PAID)){
            throw new OrderDomainException("Order is not in correct state for approve operataion");
        }
        orderStatus = APPROVED;
    }

    public void initCancel(List<String> failureMessages){
        if (!orderStatus.equals(PAID)){
            throw new OrderDomainException("Order is not in correct state for initCancel operataion");
        }
        orderStatus = CANCELLING;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages!=null ) {
            this.failureMessages.addAll(failureMessages.stream().filter(failureMessage -> !failureMessage.isEmpty()).toList());
        }
        if(this.failureMessages == null ){
            this.failureMessages=failureMessages;
        }
    }

    public void cancel(List<String> failureMessages){
        if (!orderStatus.equals(CANCELLING)){
            throw new OrderDomainException("Order is not in correct state for cancel operataion");
        }
        orderStatus = CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void validateItemsPrice() {
        Money orderItemsTotalPrice=items.stream()
                .map((orderItem)->{
                    validateItemPrice(orderItem);
                    return orderItem.getSubtotal();
                })
                .reduce(Money.ZERO, Money::add);
        if (!price.equals(orderItemsTotalPrice)){
            throw new OrderDomainException("Total price:"+price+" is not equal to order items price:"+orderItemsTotalPrice);
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid(orderItem.getPrice())){
            throw new OrderDomainException("Item price:"+orderItem.getPrice()+" is not valid for product:"
                    +orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if (price==null || !price.isGreatherThanZero()){
            throw new OrderDomainException("Total price should be greater than zero");
        }
    }


    private void validateInitialOrder() {
        if (orderStatus == null || getId() == null) {
            throw new OrderDomainException("Order is not initialized properly");
        }
    }

    private void initializeOrderItems() {
        long itemId =1;
        for(OrderItem orderItem:items){
            orderItem.initializeOrderItem(this.getId(),new OrderItemId(itemId++));
        }
    }



    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }


    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
