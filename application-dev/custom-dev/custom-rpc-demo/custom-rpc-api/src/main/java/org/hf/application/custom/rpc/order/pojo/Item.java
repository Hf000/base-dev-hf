package org.hf.application.custom.rpc.order.pojo;

import java.util.Arrays;

public class Item implements java.io.Serializable{

    private static final long serialVersionUID = -1768340664273809021L;

    private Long itemId;
    private String title;
    private String[] pics;
    private Long price;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", title='" + title + '\'' +
                ", pics=" + Arrays.toString(pics) +
                ", price=" + price +
                '}';
    }
}
