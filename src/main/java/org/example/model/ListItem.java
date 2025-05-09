package org.example.model;

public class ListItem {
    private Long id;
    private boolean selected;
    private int order;

    public ListItem(Long id) {
        this.id = id;
        this.selected = false;
        this.order = id.intValue(); // По умолчанию порядок соответствует id
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    // Метод для получения текстового представления элемента для поиска
    public String getDisplayText() {
        return "Item " + id;
    }
}
