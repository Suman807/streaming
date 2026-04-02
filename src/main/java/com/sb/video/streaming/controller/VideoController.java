package com.sb.video.streaming.controller;

import com.sb.video.streaming.model.Video;
import com.sb.video.streaming.payload.CustomMessage;
import com.sb.video.streaming.services.impl.VideoServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Slf4j
@RestController
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true")
@Tag(name = "Video APIs", description = "APIs related to video upload and retrieval")
public class VideoController implements VideoBaseController{

   @Autowired
    private VideoServiceImpl videoService;

    @GetMapping
    public ResponseEntity<?> getAllVideos(){
        return ResponseEntity.status(HttpStatus.OK).body(videoService.getAll());
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("description") String description){
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());
        video.setUploadDate(java.time.LocalDate.now());

        Video savedVideo = videoService.save(video, file);

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
