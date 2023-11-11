package ru.kishko.photoservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttachmentDTOShort {

    private String id;

    private String downloadURL;

    private String status;

    private String camName;

    private double percent;

}
