package com.sb.video.streaming.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadVideo(MultipartFile file) throws IOException;
}
