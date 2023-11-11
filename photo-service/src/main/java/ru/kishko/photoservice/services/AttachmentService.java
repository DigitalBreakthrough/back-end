package ru.kishko.photoservice.services;

import org.springframework.web.multipart.MultipartFile;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.dtos.AttachmentDTOShort;
import ru.kishko.photoservice.dtos.ResponseData;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;

public interface AttachmentService {
    AttachmentDTO saveAttachment(MultipartFile file) throws Exception;

    AttachmentDTO getAttachment(String fileId) throws AttachmentNotFoundException;

    AttachmentDTOShort updateAttachmentById(String attachmentId, AttachmentDTOShort attachmentDTOShort, byte[] changeData) throws AttachmentNotFoundException;

    ResponseData sendAttachments(ResponseData responseData);
}
