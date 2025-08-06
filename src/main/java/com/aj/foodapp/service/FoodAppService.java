package com.aj.foodapp.service;

import com.aj.foodapp.requestObject.FoodRequest;
import com.aj.foodapp.responseObject.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodAppService {

    public String storeFileInFileSystem(MultipartFile file);

    public FoodResponse uploadNewFood(FoodRequest foodRequest, MultipartFile file);

    public List<FoodResponse> getAllFoods();

    public  FoodResponse getFoodById(String foodId);

    public void deleteFoodById(String foodId);

}
