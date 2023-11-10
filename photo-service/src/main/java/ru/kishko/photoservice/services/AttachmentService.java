package ru.kishko.photoservice.services;

import org.springframework.web.multipart.MultipartFile;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws AttachmentNotFoundException;
}
