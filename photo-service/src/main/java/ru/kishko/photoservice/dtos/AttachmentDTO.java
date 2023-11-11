package ru.kishko.photoservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentDTO {

    private String id;

    private String fileName;

    private String fileType;

    private String status;

    private double percent;

    private String camName;

    @JsonIgnore
    private byte[] data;

}
