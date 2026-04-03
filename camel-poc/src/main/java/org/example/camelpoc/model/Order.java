package org.example.camelpoc.model;

public class Order {

    private String orderId;
    private String product;
    private int quantity;
    private String type;

    public Order() {
    }

    public Order(String orderId, String product, int quantity, String type) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.type = type;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Order{orderId='%s', product='%s', quantity=%d, type='%s'}"
                .formatted(orderId, product, quantity, type);
    }
}
