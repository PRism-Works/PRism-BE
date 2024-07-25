package com.prismworks.prism.domain.prism.dto;
import lombok.Data;

import java.util.List;

@Data
public class RadialDataDto {
    private int leadership;
    private int reliability;
    private int teamwork;
    private List<String> keywords;
    private String evaluation;

}
