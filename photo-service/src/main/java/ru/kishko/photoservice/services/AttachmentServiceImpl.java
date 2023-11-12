package ru.kishko.photoservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kishko.photoservice.config.AttachmentMapper;
import ru.kishko.photoservice.controllers.AttachmentController;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.dtos.AttachmentDTOShort;
import ru.kishko.photoservice.dtos.ResponseData;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;
import ru.kishko.photoservice.repositories.AttachmentRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private WebClient webMLClient;

    @Override
    public AttachmentDTO saveAttachment(MultipartFile file, String camName) throws Exception {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            throw new Exception("Filename contains invalid path sequence " + fileName);
        }

        try {
            Attachment attachment = Attachment.builder()
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .camName(camName)
                    .build();

            attachmentRepository.save(attachment);

            return attachmentMapper.toAttachmentDTO(attachment);

        } catch (Exception e) {
            throw new Exception("Couldn't save a file " + fileName + " \n " + e);
        }

    }

    @Override
    public AttachmentDTO getAttachment(String fileId) throws AttachmentNotFoundException {

        Attachment attachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new AttachmentNotFoundException("File not found with id: " + fileId));

        return attachmentMapper.toAttachmentDTO(attachment);
    }

    @Override
    public AttachmentDTOShort updateAttachmentById(String fileId, String status, double percent, String camName, String bytes) throws AttachmentNotFoundException {

        AttachmentDTO attachmentDB = getAttachment(fileId);

        System.out.println(percent + " " + status);

        if (status != null && !"".equals(status)) {
            attachmentDB.setStatus(status);
        }

        if (percent > 0) {
            attachmentDB.setPercent(percent);
        }

        if (Objects.nonNull(camName) && !"".equals(camName)) {
            attachmentDB.setCamName(camName);
        }

        attachmentDB.setData(Base64.getDecoder().decode(bytes.getBytes(StandardCharsets.UTF_8)));

        attachmentRepository.save(attachmentMapper.toAttachment(attachmentDB));

        return AttachmentDTOShort.builder()
                .downloadURL(AttachmentController.createDownloadURL(fileId))
                .status(attachmentDB.getStatus())
                .percent(attachmentDB.getPercent())
                .camName(attachmentDB.getCamName())
                .build();
    }

    @Override
    public ResponseData sendAttachments(ResponseData responseData) {

        System.out.println(responseData);

        return webMLClient.post()
                .uri("/uploadImage")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseData)
                .retrieve()
                .bodyToMono(ResponseData.class)
                .block();
    }


}
