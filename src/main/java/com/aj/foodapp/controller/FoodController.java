package com.aj.foodapp.controller;

import com.aj.foodapp.requestObject.FoodRequest;
import com.aj.foodapp.responseObject.FoodResponse;
import com.aj.foodapp.service.FoodAppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/foodapp/api")
public class FoodController {

    @Autowired
    FoodAppService foodAppService;

    Logger log = LoggerFactory.getLogger(FoodController.class);

    @PostMapping("/uploadfood")
    public ResponseEntity<FoodResponse> uploadNewFood(@RequestPart("food") String foodString, @RequestPart("file") MultipartFile file, HttpServletRequest httpServletRequest)
    {
        log.info("Request URL : "+httpServletRequest.getRemoteAddr()+":"+httpServletRequest.getLocalPort()+""+httpServletRequest.getRequestURI());

        ObjectMapper objectMapper = new ObjectMapper();

        FoodRequest foodRequest = null;

        try {
             foodRequest = objectMapper.readValue(foodString,FoodRequest.class);

            log.info("Food Request : "+foodRequest.toString());
        }
        catch (JsonProcessingException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON Format");
        }

        FoodResponse foodResponse = foodAppService.uploadNewFood(foodRequest,file);

        log.info("Food Response : "+foodResponse);

        ResponseEntity<FoodResponse> foodResponseEntity = new ResponseEntity<>(foodResponse, HttpStatus.CREATED);

        log.info("Food Response Entity : "+foodResponseEntity.toString());

        return foodResponseEntity;
    }

}
