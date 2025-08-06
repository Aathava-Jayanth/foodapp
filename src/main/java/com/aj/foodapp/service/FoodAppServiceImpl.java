package com.aj.foodapp.service;

import com.aj.foodapp.entity.FoodEntity;
import com.aj.foodapp.configuration.FileStorageConfig;
import com.aj.foodapp.exception.CustomFileStorageException;
import com.aj.foodapp.exception.FoodNotFoundException;
import com.aj.foodapp.repository.FoodRepository;
import com.aj.foodapp.requestObject.FoodRequest;
import com.aj.foodapp.responseObject.FoodResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // My Implementation for location
    // private String location = "D:/SpringBoot React Projects/foodapp_images/";
    // private final Path rootLocation = Paths.get(location);

    // Method to Store Image File In File System
    public String storeFileInFileSystem(MultipartFile file) {

        Path destinationFile;

        try {

            if (file.isEmpty()) {
                throw new CustomFileStorageException("Failed to store empty file.");
            }

            // Store The File With Original File Name
            // destinationFile = this.rootLocation.resolve(
            //                Paths.get(file.getOriginalFilename()))
            //        .normalize().toAbsolutePath();


            // Custom File Name Creation
            String fileName  = getCustomFileName();
            String fileExtention = getFileExtention(file);


            destinationFile = this.rootLocation.resolve(
                            Paths.get(generateCustomFileNameWithExtention(fileName,fileExtention)))
                    .normalize().toAbsolutePath();


            log.info("Destination File Created : "+destinationFile.toString());


            if ( ! destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()) ) {
                // This is a security check
                throw new CustomFileStorageException(
                        "Cannot store file outside current directory.");
            }


            // Try Block to execute some thing in class
            try (InputStream inputStream = file.getInputStream()) {

                log.info("File (Info) Name : "+file.getName()+" "+file.getOriginalFilename()+" "+file.getContentType());
                log.info("Custom File (Info) Name : "+fileName+" "+fileExtention+" "+file.getContentType());

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

    // Method Image File From File System
    private boolean deleteFileFromFileSystem(String foodImageURL)
    {
        try {

            Path filePath = Paths.get(foodImageURL);

            log.info("Delete File Path {}",filePath);

            if (Files.exists(filePath)) {

                Files.delete(filePath); // File deleted successfully
                log.info("File Deleted Successfully");

            } else {

                log.error("File not found : {}",foodImageURL); // File does not exist

            }

        } catch (Exception e)
        {
            log.error("Failed to delete file : " + foodImageURL + " - " + e.getMessage());
            throw new CustomFileStorageException(e.getMessage(),e);
        }

        return true;
    }

    // Method to Upload Food Request
    @Override
    public FoodResponse uploadNewFood(FoodRequest foodRequest, MultipartFile file) {

        log.info("Food Upload Started for : "+foodRequest.toString());

        FoodEntity newFoodEntity = convertFoodEntity(foodRequest);

        String foodImageUrl = storeFileInFileSystem(file);

        log.info("Food Image URL : {}",foodImageUrl);

        newFoodEntity.setImageUrl(foodImageUrl);

        newFoodEntity = foodRepository.save(newFoodEntity);

        log.info("Food Upload Ended for : "+foodRequest.toString());

        return convertFoodResponse(newFoodEntity);
    }

    @Override
    public List<FoodResponse> getAllFoods() {

        List<FoodResponse> listOfFoods = foodRepository.findAll()
                .stream()
                .map(this::convertFoodResponse)
                .toList();

        return listOfFoods;

    }

    @Override
    public FoodResponse getFoodById(String foodId) {

        FoodEntity foodEntity = foodRepository.findById(foodId).orElse(null);

        if (foodEntity == null)
        {
            throw new FoodNotFoundException("Food Not Found With Id : "+foodId);
        }

        FoodResponse foodResponse = convertFoodResponse(foodEntity);

        return foodResponse;
    }

    @Override
    public void deleteFoodById(String foodId) {

        FoodResponse foodResponse = this.getFoodById(foodId);

        String foodImageURL = foodResponse.getImageUrl();

        boolean isFoodImageDeleted = false;

        isFoodImageDeleted = deleteFileFromFileSystem(foodImageURL);

        if(isFoodImageDeleted)
        {
            foodRepository.deleteById(foodResponse.getId());
            log.info("Food Deleted Successfully");
        }

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

    public String getFileExtention(MultipartFile file)
    {
        String fileExtention = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);

        return fileExtention;
    }

    public String getCustomFileName()
    {
        String fileName = UUID.randomUUID().toString();
        // String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        return fileName;
    }

    public String generateCustomFileNameWithExtention(String fileName,String fileExtention)
    {
        return fileName+"."+fileExtention;
    }

}
