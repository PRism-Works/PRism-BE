package com.prismworks.prism.domain.email.dto;

import com.prismworks.prism.domain.email.model.EmailTemplate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
public class EmailSendRequest {
    private String fromEmail;

    @NotEmpty
    private String toEmail;

    @NotNull
    private EmailTemplate template;

    private Map<String, Object> templateVariables;


    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
}
