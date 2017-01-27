package id.co.tirezone.tirezonemobile;

/**
 * Created by Jessica on 13-Jan-17.
 */

public class Product {
    private String pattern;
    private int price;
    private String size;
    private int stock;

    public Product() {
    }

    public Product(String pattern, int price, String size, int stock) {
        this.pattern = pattern;
        this.price = price;
        this.size = size;
        this.stock = stock;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
