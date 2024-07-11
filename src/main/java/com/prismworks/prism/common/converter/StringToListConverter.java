package com.prismworks.prism.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringToListConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if(!StringUtils.hasText(dbData)) {
            return Collections.emptyList();
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
