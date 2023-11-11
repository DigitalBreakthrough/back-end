package com.kishko.photoservice.services;

import com.kishko.photoservice.entities.Video;
import com.kishko.photoservice.repositories.VideoRepository;
import com.kishko.photoservice.responses.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private WebClient webMLClient;

    @Override
    public Video saveVideo(MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            throw new Exception("Filename contains invalid path sequence " + fileName);
        }

        try {
            Video video = Video.builder()
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .build();

            return videoRepository.save(video);

        } catch (Exception e) {
            throw new Exception("Couldn't save a file " + fileName + " \n " + e);
        }

    }

    @Override
    public Video getVideoById(String fileId) {
        return videoRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + fileId));
    }

    @Override
    public String sendVideo(ResponseData responseData) {

        webMLClient.post()
                .uri("/uploadVideo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseData)
                .retrieve()
                .bodyToMono(ResponseData.class)
                .subscribe(response -> {
                    System.out.println("Response: " + response);
                });

        return "sent successfully";
    }

    @Override
    public ResponseData getVideo() {
        return webMLClient.get()
                .uri("/getVideo")
                .retrieve()
                .bodyToMono(ResponseData.class)
                .block();
    }

}



