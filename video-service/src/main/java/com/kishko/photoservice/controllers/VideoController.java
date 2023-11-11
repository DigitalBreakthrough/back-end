package com.kishko.photoservice.controllers;

import com.kishko.photoservice.entities.Video;
import com.kishko.photoservice.responses.ResponseData;
import com.kishko.photoservice.responses.TimeCode;
import com.kishko.photoservice.responses.VideoResult;
import com.kishko.photoservice.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/videos")
@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        Video video = videoService.saveVideo(file);
        String downloadURL = createDownloadURL(video.getId());
        return new ResponseData(
                video.getId(),
                "video",
                new VideoResult(downloadURL, "", new ArrayList<>(List.of(new TimeCode("11:12", 1, "")))));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) {
        Video video = videoService.getVideoById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(video.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + video.getFileName() + "\"")
                .body(new ByteArrayResource(video.getData()));
    }

    public static String createDownloadURL(String fileId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/videos/download/")
                .path(fileId)
                .toUriString();
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendVideo(@RequestBody ResponseData responseData) {
        return new ResponseEntity<>(videoService.sendVideo(responseData), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseData> getVideo() {
        return new ResponseEntity<>(videoService.getVideo(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseData> updateResponseDataById(@RequestParam("fileId") String fileId, @RequestBody ResponseData responseData) {
        return new ResponseEntity<>(videoService.updateResponseDataById(fileId, responseData), HttpStatus.OK);
    }

}
