package com.sb.video.streaming.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.sb.video.streaming.services.CloudinaryService;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @CircuitBreaker(name = "cloudinaryCB")
    @Retry(name = "cloudinaryRetry")
    @Bulkhead(name = "cloudinaryBulkhead")
    @RateLimiter(name = "cloudinaryRateLimiter", fallbackMethod = "rateLimitFallback")
    @Override
    public String uploadVideo(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), com.cloudinary.utils.ObjectUtils.asMap("resource_type", "video",
                        "folder", "videos"));
        return uploadResult.get("secure_url").toString();
    }

    // Fallback method for rate limiting
    public String rateLimitFallback(MultipartFile file, Throwable ex) {
        // Log the exception (ex) as needed
        log.error("Rate limit exceeded for Cloudinary API. Upload failed for file: {}", file.getOriginalFilename(), ex);
        return null; // Return null or a default URL indicating the upload failed due to rate limiting
    }
}
