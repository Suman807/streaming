package com.sb.video.streaming.services.impl;

import com.sb.video.streaming.model.Video;
import com.sb.video.streaming.repository.VideoRepository;
import com.sb.video.streaming.services.VideoService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    // @Value("${files.video}")
    // String DIR;

    // @PostConstruct
    // public void init(){
    //     File file = new File(DIR);
    //     if(!file.exists()){
    //         file.mkdirs();
    //         System.out.println("Created directory: " + DIR);
    //     }else{
    //         System.out.println("Directory already exists: " + DIR);
    //     }
    // }

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinaryService;

    @Override
    public Video save(Video video, MultipartFile file) {
        //Original filename
        try{
            // String fileName = file.getOriginalFilename();
            // String contentType = file.getContentType();
            // InputStream inputStream = file.getInputStream();

            // //clean filepath
            // String cleanFileName = StringUtils.cleanPath(fileName);
            // String cleanFolder = StringUtils.cleanPath(DIR);

            // Path path = Paths.get(cleanFolder,cleanFileName);

            // System.out.println(path);

            // // Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            // video.setContentType(contentType);
            // video.setFilePath(path.toString());

            String contentType = file.getContentType();
            video.setContentType(contentType);
            String videoUrl = cloudinaryService.uploadVideo(file);
            video.setUrl(videoUrl);

            return videoRepository.save(video);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Video get(String videoId) {
        return videoRepository.findById(videoId).orElse(null);
    }

    @Override
    public Video getByTitle(String title) {
        return videoRepository.findByTitle(title).orElse(null);
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }
}
