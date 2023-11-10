package ru.kishko.photoservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.entities.Status;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;
import ru.kishko.photoservice.repositories.AttachmentRepository;

import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            throw new Exception("Filename contains invalid path sequence " + fileName);
        }

        try {
            Attachment attachment = Attachment.builder()
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .build();

            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new Exception("Couldn't save a file " + fileName + " \n " + e);
        }

    }

    @Override
    public Attachment getAttachment(String fileId) throws AttachmentNotFoundException {
        return attachmentRepository.findById(fileId)
                .orElseThrow(() -> new AttachmentNotFoundException("File not found with id: " + fileId));
    }

    @Override
    public AttachmentDTO updateAttachmentByDownloadURL(String downloadURL, AttachmentDTO attachmentDTO) throws AttachmentNotFoundException {

        Attachment attachment = getAttachment(downloadURL.substring(43));

        String status = attachmentDTO.getStatus();
        double percent = attachmentDTO.getPercent();

        if (status != null) {
            attachment.setStatus(Status.valueOf(status));
        }

        if (percent > 0) {
            attachment.setPercent(percent);
        }

        attachmentRepository.save(attachment);

        return AttachmentDTO.builder()
                .downloadURL(downloadURL)
                .status(attachment.getStatus().toString())
                .percent(attachment.getPercent())
                .build();
    }
}
