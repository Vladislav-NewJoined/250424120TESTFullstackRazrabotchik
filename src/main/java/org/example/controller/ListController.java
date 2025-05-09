package org.example.controller;

import org.example.model.ListItem;
import org.example.service.ListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin
public class ListController {

    @Autowired
    private ListItemService service;

    @GetMapping
    public Map<String, Object> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        List<ListItem> items = service.getItems(page, size, search);
        int totalCount = service.getTotalCount(search);
        boolean hasMore = (page + 1) * size < totalCount;

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalCount", totalCount);
        response.put("hasMore", hasMore);

        return response;
    }

    @PostMapping("/{id}/toggle")
    public void toggleItem(@PathVariable Long id) {
        service.toggleSelection(id);
    }

    @PostMapping("/reorder")
    public void reorderItems(@RequestBody List<Long> newOrder) {
        service.updateOrder(newOrder);
    }

    @PostMapping("/reset-order")
    public void resetOrder() {
        service.resetOrder();
    }

    @GetMapping("/selected")
    public List<ListItem> getSelectedItems() {
        return service.getSelectedItems();
    }

    @GetMapping("/order")
    public List<Long> getCurrentOrder() {
        return service.getCurrentOrder();
    }
}

