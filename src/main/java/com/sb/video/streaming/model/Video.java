package com.sb.video.streaming.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Video {
    @Id
    private String videoId;
    private String title;
    private String description;
    private String contentType;
    // private String filePath;
    private String url;
    private LocalDate uploadDate;
}
