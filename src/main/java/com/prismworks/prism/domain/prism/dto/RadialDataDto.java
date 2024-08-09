package com.prismworks.prism.domain.prism.dto;
import lombok.Data;

import java.util.List;

@Data
public class RadialDataDto {
    private float leadership;
    private float reliability;
    private float teamwork;
    private List<String> keywords;
    private String evaluation;

}
