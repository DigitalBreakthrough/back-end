package ru.kishko.photoservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kishko.photoservice.dtos.AttachmentDTO;
import ru.kishko.photoservice.entities.Attachment;
import ru.kishko.photoservice.entities.Status;

@Component
@RequiredArgsConstructor
public class AttachmentMapper {

    public Attachment toAttachment(AttachmentDTO attachmentDTO) {
        return Attachment.builder()
                .id(attachmentDTO.getId())
                .fileType(attachmentDTO.getFileType())
                .fileName(attachmentDTO.getFileName())
                .data(attachmentDTO.getData())
                .percent(attachmentDTO.getPercent())
                .status(Status.valueOf(attachmentDTO.getStatus()))
                .camName(attachmentDTO.getCamName())
                .build();
    }

    public AttachmentDTO toAttachmentDTO(Attachment attachment) {
        return AttachmentDTO.builder()
                .id(attachment.getId())
                .fileType(attachment.getFileType())
                .fileName(attachment.getFileName())
                .data(attachment.getData())
                .percent(attachment.getPercent())
                .status(attachment.getStatus().toString())
                .camName(attachment.getCamName())
                .build();
    }
}
