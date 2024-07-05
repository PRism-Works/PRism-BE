package com.prismworks.prism.domain.email.util;

import com.prismworks.prism.domain.email.model.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class EmailTemplateConverter {

    private final SpringTemplateEngine templateEngine;

    @Value("${service.frontend-origin}")
    private String frontendOrigin;

    public String convertToStr(EmailTemplate templateByPath, Map<String, Object> variables) {
        String templateName = templateByPath.getTemplateName();
        boolean frontOriginRequired = templateByPath.isFrontOriginRequired();

        if(frontOriginRequired) {
            variables.put("frontendOrigin", frontendOrigin);
        }

        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process(templateName, context);
    }
}
