package com.kishko.photoservice.services;

import com.kishko.photoservice.entities.Video;
import com.kishko.photoservice.responses.ResponseData;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    public Video saveVideo(MultipartFile file) throws Exception;

    Video getVideoById(String fileId);

    String sendVideo(ResponseData responseData);

    ResponseData getVideo();

}
