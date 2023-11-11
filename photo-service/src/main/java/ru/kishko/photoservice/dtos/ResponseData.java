package ru.kishko.photoservice.dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseData {

    private String type;

    private List<AttachmentDTOShort> attachments;

}
