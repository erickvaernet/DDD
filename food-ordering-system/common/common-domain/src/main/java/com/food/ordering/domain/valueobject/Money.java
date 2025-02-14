package com.food.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    //final por que los value objects son inmutables
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    //Dato curioso hay que usar compareTo para ver igualdades de BigDecimal debido a que
    // no se puede usar equals eso por que apra equals 1 y 1.00 son diferentes
    public boolean isGreatherThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }


    public boolean isGreatherThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(Money money) {
        return new Money(this.amount.add(money.getAmount()));
    }

    public Money substract(Money money) {
        return new Money(money.amount.subtract(this.amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

}
