package com.prismworks.prism.domain.prism.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PrismDataDto {
    private Map<String, Integer> prismData;
    private RadialDataDto radialData;
}
