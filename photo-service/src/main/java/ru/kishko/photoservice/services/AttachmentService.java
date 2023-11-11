package ru.kishko.photoservice.services;

import org.springframework.web.multipart.MultipartFile;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.dtos.AttachmentDTOShort;
import ru.kishko.photoservice.dtos.ResponseData;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;

import java.io.IOException;

public interface AttachmentService {
    AttachmentDTO saveAttachment(MultipartFile file) throws Exception;

    AttachmentDTO getAttachment(String fileId) throws AttachmentNotFoundException;

    ResponseData sendAttachments(ResponseData responseData);

    AttachmentDTOShort updateAttachmentById(String fileId, String status, double percent, String camName, String bytes) throws AttachmentNotFoundException;
}
