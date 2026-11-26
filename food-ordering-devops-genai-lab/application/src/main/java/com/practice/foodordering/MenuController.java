package com.practice.foodordering;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "application", "food-ordering-app");
    }

    @GetMapping("/api/menu")
    public List<Map<String, Object>> menu() {
        return List.of(
            Map.of("id", 1, "name", "Veg Biryani", "price", 180),
            Map.of("id", 2, "name", "Paneer Wrap", "price", 140),
            Map.of("id", 3, "name", "Chicken Biryani", "price", 220)
        );
    }
}
