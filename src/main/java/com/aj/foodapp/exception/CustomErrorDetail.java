package com.aj.foodapp.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomErrorDetail {

    private String message;
    private String description;
    private LocalDateTime timeStamp;

}
