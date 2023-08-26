package org.hf.application.custom.rpc.order.pojo;

public class Order implements java.io.Serializable{

    private static final long serialVersionUID = 7677458290162024601L;
    private String orderId;
    private Long userId;
    private Long date;
    private Integer itemCount;
    private Long totalPrice;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId=" + userId +
                ", date=" + date +
                ", itemCount=" + itemCount +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
