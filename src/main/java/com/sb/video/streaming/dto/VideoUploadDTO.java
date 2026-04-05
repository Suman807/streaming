package com.sb.video.streaming.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoUploadDTO {
    @NotNull(message = "File is required and cannot be null")
    MultipartFile file;
    @NotBlank(message = "Title is required and cannot be blank")
    String title;   
    @NotBlank(message = "Description is required and cannot be blank")
    String description;
}
