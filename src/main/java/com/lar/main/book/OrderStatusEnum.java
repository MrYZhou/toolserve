package com.lar.main.book;

public enum OrderStatusEnum {
    UN_PAID(0, "订单未支付"),
    PAIDED(1, "订单已支付"),
    SENDED(2, "已发货"),
    ;

    private final int code;
    private final String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    OrderStatusEnum(int index, String desc) {
        this.code = index;
        this.desc = desc;
    }

    public static OrderStatusEnum getOrderStatusEnum(int orderStatusCode) {
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()) {
            if (statusEnum.getCode() == orderStatusCode) {
                return statusEnum;
            }
        }
        return null;
    }
}
