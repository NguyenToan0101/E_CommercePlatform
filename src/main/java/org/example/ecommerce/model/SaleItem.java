package org.example.ecommerce.model;

public class SaleItem {
    private Integer id;
    private String title;
    private int increment;

    public SaleItem(Integer id, String title, int increment) {
        this.id = id;
        this.title = title;
        this.increment = increment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}