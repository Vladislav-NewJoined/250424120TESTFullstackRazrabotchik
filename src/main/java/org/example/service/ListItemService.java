package org.example.service;

import org.example.model.ListItem;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListItemService {
    private final List<ListItem> items;
    private List<Long> customOrder = new ArrayList<>(); // Для хранения пользовательского порядка
    private boolean hasCustomOrder = false; // Флаг, указывающий, что есть пользовательская сортировка

    public ListItemService() {
        items = new ArrayList<>();
        for (long i = 1; i <= 1_000_000; i++) {
            items.add(new ListItem(i));
        }
    }

    public List<ListItem> getItems(int page, int size, String searchQuery) {
        // Сначала применяем поисковый фильтр, если он есть
        List<ListItem> filteredItems;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            String query = searchQuery.toLowerCase();
            filteredItems = items.stream()
                    .filter(item -> item.getDisplayText().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        } else {
            filteredItems = new ArrayList<>(items);
        }

        // Затем применяем пользовательскую сортировку, если она есть
        if (hasCustomOrder && !customOrder.isEmpty()) {
            // Создаем Map для быстрого поиска индекса
            Map<Long, Integer> orderMap = new HashMap<>();
            for (int i = 0; i < customOrder.size(); i++) {
                orderMap.put(customOrder.get(i), i);
            }

            filteredItems.sort((a, b) -> {
                Integer aIndex = orderMap.get(a.getId());
                Integer bIndex = orderMap.get(b.getId());

                // Если элемент не найден в порядке, помещаем его в конец
                if (aIndex == null && bIndex == null) {
                    return a.getId().compareTo(b.getId()); // Сортируем по ID
                } else if (aIndex == null) {
                    return 1; // a в конец
                } else if (bIndex == null) {
                    return -1; // b в конец
                } else {
                    return aIndex.compareTo(bIndex);
                }
            });
        }

        // Наконец, применяем пагинацию
        int start = page * size;
        int end = Math.min(start + size, filteredItems.size());
        if (start >= filteredItems.size()) {
            return new ArrayList<>();
        }
        return filteredItems.subList(start, end);
    }

    // Метод для получения общего количества элементов после фильтрации
    public int getTotalCount(String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            String query = searchQuery.toLowerCase();
            return (int) items.stream()
                    .filter(item -> item.getDisplayText().toLowerCase().contains(query))
                    .count();
        }
        return items.size();
    }

    public void toggleSelection(Long id) {
        items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .ifPresent(item -> item.setSelected(!item.isSelected()));
    }

    public void updateOrder(List<Long> newOrder) {
        if (newOrder != null && !newOrder.isEmpty()) {
            this.customOrder = new ArrayList<>(newOrder);
            this.hasCustomOrder = true;
        }
    }

    public List<ListItem> getSelectedItems() {
        List<ListItem> selectedItems = items.stream()
                .filter(ListItem::isSelected)
                .collect(Collectors.toList());

        if (hasCustomOrder && !customOrder.isEmpty()) {
            // Создаем Map для быстрого поиска индекса
            Map<Long, Integer> orderMap = new HashMap<>();
            for (int i = 0; i < customOrder.size(); i++) {
                orderMap.put(customOrder.get(i), i);
            }

            selectedItems.sort((a, b) -> {
                Integer aIndex = orderMap.get(a.getId());
                Integer bIndex = orderMap.get(b.getId());

                // Если элемент не найден в порядке, помещаем его в конец
                if (aIndex == null && bIndex == null) {
                    return a.getId().compareTo(b.getId()); // Сортируем по ID
                } else if (aIndex == null) {
                    return 1; // a в конец
                } else if (bIndex == null) {
                    return -1; // b в конец
                } else {
                    return aIndex.compareTo(bIndex);
                }
            });
        }

        return selectedItems;
    }

    public boolean hasSelectedItems() {
        return items.stream().anyMatch(ListItem::isSelected);
    }

    public List<Long> getCurrentOrder() {
        return new ArrayList<>(customOrder);
    }

    public void resetOrder() {
        this.customOrder.clear();
        this.hasCustomOrder = false;
    }
}
