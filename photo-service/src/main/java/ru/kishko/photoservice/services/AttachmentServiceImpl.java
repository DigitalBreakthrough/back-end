package ru.kishko.photoservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kishko.photoservice.config.AttachmentMapper;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.dtos.AttachmentDTOShort;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.errors.AttachmentNotFoundException;
import ru.kishko.photoservice.repositories.AttachmentRepository;

import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public AttachmentDTO saveAttachment(MultipartFile file) throws Exception {

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
    public AttachmentDTOShort updateAttachmentByDownloadURL(String downloadURL, AttachmentDTOShort attachmentDTOShort) throws AttachmentNotFoundException {

        AttachmentDTO attachmentDB = getAttachment(downloadURL.substring(43));

        String status = attachmentDTOShort.getStatus();
        double percent = attachmentDTOShort.getPercent();

        if (status != null) {
            attachmentDB.setStatus(status);
        }

        if (percent > 0) {
            attachmentDB.setPercent(percent);
        }

        attachmentRepository.save(attachmentMapper.toAttachment(attachmentDB));

        return AttachmentDTOShort.builder()
                .downloadURL(downloadURL)
                .status(attachmentDB.getStatus())
                .percent(attachmentDB.getPercent())
                .build();
    }
}
