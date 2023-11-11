package ru.kishko.photoservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.dtos.AttachmentDTOShort;
import ru.kishko.photoservice.dtos.ResponseData;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;
import ru.kishko.photoservice.services.AttachmentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/attachments")
@CrossOrigin
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        AttachmentDTO attachment = attachmentService.saveAttachment(file);
        String downloadURL = createDownloadURL(attachment.getId());
        return new ResponseData(
                "image",
                List.of(new AttachmentDTOShort(attachment.getId(), downloadURL, attachment.getStatus(), attachment.getCamName(), attachment.getPercent())));
    }

    @PostMapping("/uploads")
    public ResponseEntity<ResponseData> uploadFiles(@RequestParam("file") MultipartFile ... files) throws Exception {
        List<AttachmentDTO> attachments = new ArrayList<>();
        List<AttachmentDTOShort> attachmentDTOShorts = new ArrayList<>();
        for (MultipartFile file : files) {
            AttachmentDTO attachment = attachmentService.saveAttachment(file);
            attachments.add(attachment);
        }
        for (AttachmentDTO attachment : attachments) {
            String downloadURL = createDownloadURL(attachment.getId());
            attachmentDTOShorts.add(new AttachmentDTOShort(attachment.getId(), downloadURL, attachment.getStatus(), attachment.getCamName(), attachment.getPercent()));
        }

        ResponseData responseData = ResponseData.builder()
                .type("images")
                .attachments(attachmentDTOShorts)
                .build();

        System.out.println(responseData);

        return sendAttachments(responseData);
    }

    public static String createDownloadURL(String fileId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/attachments/download/")
                .path(fileId)
                .toUriString();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) throws AttachmentNotFoundException {
        AttachmentDTO attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping
    public ResponseEntity<AttachmentDTO> getAttachmentById(@RequestParam("fileId") String fileId) throws Exception {
        return new ResponseEntity<>(attachmentService.getAttachment(fileId), HttpStatus.OK);
    }

    @GetMapping("/send")
    public ResponseEntity<ResponseData> sendAttachments(@RequestBody ResponseData responseData) {
        return new ResponseEntity<>(attachmentService.sendAttachments(responseData), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AttachmentDTOShort> updateAttachmentById(@RequestParam("fileId") String fileId, @RequestParam("status") String status, @RequestParam("percent") double percent, @RequestParam("camName") String camName, @RequestParam("bytes") String bytes) throws AttachmentNotFoundException {
        return new ResponseEntity<>(attachmentService.updateAttachmentById(fileId, status, percent, camName, bytes), HttpStatus.OK);
    }
}
