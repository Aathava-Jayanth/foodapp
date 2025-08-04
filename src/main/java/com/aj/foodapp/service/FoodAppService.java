package com.aj.foodapp.service;

import com.aj.foodapp.requestObject.FoodRequest;
import com.aj.foodapp.responseObject.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FoodAppService {

    public String storeFileInFileSystem(MultipartFile file);

    public FoodResponse uploadNewFood(FoodRequest foodRequest, MultipartFile file);

}
