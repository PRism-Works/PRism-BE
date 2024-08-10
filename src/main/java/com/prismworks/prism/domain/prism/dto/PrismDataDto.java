package com.prismworks.prism.domain.prism.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PrismDataDto {
    private Map<String, Float> prismData;
    private RadialDataDto radialData;
}
