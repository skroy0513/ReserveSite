package kr.co.hugetraffic.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("pending"),
    SUCCESS("success"),
    FAIL("fail");

    private final String orderStatus;

    OrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
