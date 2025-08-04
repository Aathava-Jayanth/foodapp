package com.aj.foodapp.controller;

import com.aj.foodapp.service.FoodAppService;
import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/foodapp")
public class DummyFileUpload {

    @Autowired
    FoodAppService foodAppService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name = "file") MultipartFile file)
    {

        String finalPath = foodAppService.storeFileInFileSystem(file);

        return finalPath;
    }

}
