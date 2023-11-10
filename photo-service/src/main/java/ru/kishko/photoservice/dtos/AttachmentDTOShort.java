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
public class AttachmentDTOShort {

    @JsonIgnore
    private String id;

    private String downloadURL;

    private String status;

    private double percent;

}
