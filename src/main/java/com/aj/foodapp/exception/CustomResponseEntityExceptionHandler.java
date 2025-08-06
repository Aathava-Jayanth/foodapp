package com.aj.foodapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(exception = Exception.class)
    public final ResponseEntity<CustomErrorDetail> handleAllException(Exception exception, WebRequest request) throws Exception
    {
        CustomErrorDetail customErrorDetail = CustomErrorDetail
                .builder()
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<CustomErrorDetail>(customErrorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
