package com.aj.foodapp.requestObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequest {
    private String name;
    private String category;
    private String description;
    private double price;
}
