package com.sb.video.streaming.services;

import org.springframework.web.multipart.MultipartFile;

import com.sb.video.streaming.model.Video;

import java.util.List;

public interface VideoService {

    //save video
    Video save(Video video, MultipartFile file);

    //get video
    Video get(String videoId);

    //get video by title
    Video getByTitle(String title);

    //get All video
    List<Video> getAll();
}
