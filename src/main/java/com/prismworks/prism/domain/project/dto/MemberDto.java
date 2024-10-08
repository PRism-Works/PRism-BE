package com.prismworks.prism.domain.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class MemberDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private List<String> roles;
    private boolean anonyVisibility;
}
