package com.prismworks.prism.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MemberDetailDto {
    private String name;
    private String email;
    private List<String> roles;
}
