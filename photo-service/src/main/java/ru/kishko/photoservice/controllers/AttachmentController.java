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
import ru.kishko.photoservice.dtos.ResponseData;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;
import ru.kishko.photoservice.services.AttachmentService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/attachments")
@CrossOrigin
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Attachment attachment = attachmentService.saveAttachment(file);
        String downloadURL = createDownloadURL(attachment.getId());
        return new ResponseData(
                "image",
                List.of(new AttachmentDTO(attachment.getId(), downloadURL, attachment.getStatus().toString(), attachment.getPercent())));
    }

    @PostMapping("/uploads")
    public ResponseData uploadFiles(@RequestParam("file") MultipartFile ... files) throws Exception {
        List<Attachment> attachments = new ArrayList<>();
        List<AttachmentDTO> attachmentDTOS = new ArrayList<>();
        for (MultipartFile file : files) {
            Attachment attachment = attachmentService.saveAttachment(file);
            attachments.add(attachment);
        }
        for (Attachment attachment : attachments) {
            String downloadURL = createDownloadURL(attachment.getId());
            attachmentDTOS.add(new AttachmentDTO(attachment.getId(), downloadURL, attachment.getStatus().toString(), attachment.getPercent()));
        }
        return ResponseData.builder()
                .type("images")
                .attachments(attachmentDTOS)
                .build();
    }

    private String createDownloadURL(String fileId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/attachments/download/")
                .path(fileId)
                .toUriString();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) throws AttachmentNotFoundException {
        Attachment attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping
    public ResponseEntity<Attachment> getAttachmentById(@RequestParam("fileId") String fileId) throws Exception {
        return new ResponseEntity<>(attachmentService.getAttachment(fileId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AttachmentDTO> updateAttachmentByDownloadURL(@RequestParam("downloadURL") String downloadURL, @RequestBody AttachmentDTO attachmentDTO) throws AttachmentNotFoundException {
        return new ResponseEntity<>(attachmentService.updateAttachmentByDownloadURL(downloadURL, attachmentDTO), HttpStatus.OK);
    }
}
