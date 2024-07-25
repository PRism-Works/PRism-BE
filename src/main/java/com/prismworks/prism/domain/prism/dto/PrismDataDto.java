package com.prismworks.prism.domain.prism.dto;

import java.util.Map;

public class PrismDataDto {
    private Map<String, Integer> prismData;
    private RadialDataDto radialData;

    // Getters and Setters
    public Map<String, Integer> getPrismData() {
        return prismData;
    }

    public void setPrismData(Map<String, Integer> prismData) {
        this.prismData = prismData;
    }

    public RadialDataDto getRadialData() {
        return radialData;
    }

    public void setRadialData(RadialDataDto radialData) {
        this.radialData = radialData;
    }
}
