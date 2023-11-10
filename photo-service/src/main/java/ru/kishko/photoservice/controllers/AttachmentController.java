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
import ru.kishko.photoservice.dtos.ResponseData;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;
import ru.kishko.photoservice.services.AttachmentService;

@RestController
@RequestMapping("/attachments")
@CrossOrigin
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        Attachment attachment = attachmentService.saveAttachment(file);;

        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/attachments/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseData(attachment.getFileName(),
                downloadURL,
                file.getContentType(),
                file.getSize());

    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) throws AttachmentNotFoundException {

        Attachment attachment = attachmentService.getAttachment(fileId);;

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

}
