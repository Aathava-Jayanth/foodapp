package com.aj.foodapp.responseObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {
    private String id;
    private String name;
    private String category;
    private String description;
    private double price;
    private String imageUrl;
}
