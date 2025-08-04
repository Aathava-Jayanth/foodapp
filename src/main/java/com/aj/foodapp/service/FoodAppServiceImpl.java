package com.aj.foodapp.service;

import com.aj.foodapp.Entity.FoodEntity;
import com.aj.foodapp.configuration.FileStorageConfig;
import com.aj.foodapp.exception.CustomFileStorageException;
import com.aj.foodapp.repository.FoodRepository;
import com.aj.foodapp.requestObject.FoodRequest;
import com.aj.foodapp.responseObject.FoodResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FoodAppServiceImpl implements FoodAppService {

    private final Path rootLocation;

    private FileStorageConfig fileStorageConfig;

    private FoodRepository foodRepository;

    Logger log = LoggerFactory.getLogger(FoodAppServiceImpl.class);

    // Constructor Injection
    public FoodAppServiceImpl(FileStorageConfig fileStorageConfig,FoodRepository foodRepository) {

        if(fileStorageConfig.getLocation().trim().isEmpty()){
            throw new CustomFileStorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(fileStorageConfig.getLocation());

        log.info("Root Location : "+rootLocation.toString());

        this.foodRepository = foodRepository;
    }

//    My Implementation for location
//    private String location = "D:/SpringBoot React Projects/foodapp_images/";
//    private final Path rootLocation = Paths.get(location);

    @Override
    public String storeFileInFileSystem(MultipartFile file) {

        Path destinationFile;

        try {

            if (file.isEmpty()) {
                throw new CustomFileStorageException("Failed to store empty file.");
            }

            destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();


            log.info("Destination File Created : "+destinationFile.toString());


            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new CustomFileStorageException(
                        "Cannot store file outside current directory.");
            }


            // Try Block to execute some thing in class
            try (InputStream inputStream = file.getInputStream()) {

                log.info("File (Info) Name : "+file.getName()+" "+file.getOriginalFilename()+" "+file.getContentType());

                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);

                if (Files.exists(destinationFile)) {
                    log.info("File exists at destination, copy likely successful.");
                } else {
                    log.info("File not found at destination after copy attempt.");
                }
            }
            catch (Exception e) {
                throw new CustomFileStorageException(e.getMessage(),e);
            }

        } catch (Exception e) {
            throw new CustomFileStorageException(e.getMessage(),e);
        }

        return destinationFile.getParent().toString().concat("\\").concat(destinationFile.getFileName().toString());
    }

    @Override
    public FoodResponse uploadNewFood(FoodRequest foodRequest, MultipartFile file) {

        log.info("Food Upload Started for : "+foodRequest.toString());

        FoodEntity newFoodEntity = convertFoodEntity(foodRequest);

        String foodImageUrl = storeFileInFileSystem(file);

        newFoodEntity.setImageUrl(foodImageUrl);

        newFoodEntity = foodRepository.save(newFoodEntity);

        log.info("Food Upload Ended for : "+foodRequest.toString());

        return convertFoodResponse(newFoodEntity);
    }

    private FoodEntity convertFoodEntity(FoodRequest foodRequest)
    {
        log.info("Food Entity Preperation for Food Request: "+foodRequest.toString());

        return FoodEntity
                .builder()
                .name(foodRequest.getName())
                .category(foodRequest.getCategory())
                .description(foodRequest.getDescription())
                .price(foodRequest.getPrice())
                .build();
    }

    private FoodResponse convertFoodResponse(FoodEntity foodEntity)
    {
        log.info("Food Response Preperation for Food Entity : "+foodEntity.toString());

        return FoodResponse
                .builder()
                .id(foodEntity.getId())
                .name(foodEntity.getName())
                .category(foodEntity.getCategory())
                .description(foodEntity.getDescription())
                .price(foodEntity.getPrice())
                .imageUrl(foodEntity.getImageUrl())
                .build();
    }


}
