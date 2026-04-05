package com.sb.video.streaming.controller;

import com.sb.video.streaming.model.Video;
import com.sb.video.streaming.payload.CustomMessage;
import com.sb.video.streaming.dto.VideoUploadDTO;
import com.sb.video.streaming.services.impl.VideoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Slf4j
@RestController
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true")
@Tag(name = "Video APIs", description = "APIs related to video upload and retrieval")
public class VideoController implements VideoBaseController{

    @Autowired
    private VideoServiceImpl videoService;

    @Operation(
        summary = "Get all videos",
        description = "Retrieve a list of all uploaded videos with their metadata (title, description, upload date)."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of videos"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication credentials are missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions to access videos"),
            @ApiResponse(responseCode = "500", description = "Internal server error - failed to retrieve videos from the system")
        }
    )
    @GetMapping
    public ResponseEntity<?> getAllVideos(){
        return ResponseEntity.status(HttpStatus.OK).body(videoService.getAll());
    }

    @Operation(
        summary = "Upload a video",
        description = "Upload a video file along with metadata (title and description). The file will be processed and stored in the system."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Video uploaded and saved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - missing or invalid file, title, or description parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication credentials are missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions to upload videos"),
            @ApiResponse(responseCode = "500", description = "Internal server error - video upload or storage process failed")
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Video upload request containing file and metadata",
        required = true,
        content = @Content(
            mediaType = "multipart/form-data",
            schema = @Schema(
                implementation = VideoUploadDTO.class,
                description = "Contains the video file (MultipartFile), title, and description"
            )
        )
    )
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@Valid @ModelAttribute VideoUploadDTO videoUploadDTO){

        Video video = new Video();
        video.setTitle(videoUploadDTO.getTitle());
        video.setDescription(videoUploadDTO.getDescription());
        video.setVideoId(UUID.randomUUID().toString());
        video.setUploadDate(java.time.LocalDate.now());

        Video savedVideo = videoService.save(video, videoUploadDTO.getFile());

        if (savedVideo != null) {
            log.info("Video uploaded successfully: {}", savedVideo.getTitle());
            return ResponseEntity.status(HttpStatus.OK).body(savedVideo);
            
        }
        else {
            log.error("Video upload failed for title: {}", video.getTitle());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomMessage.builder()
                            .message("Video upload failed").success(false)
                            .build());
        }

    }
}
