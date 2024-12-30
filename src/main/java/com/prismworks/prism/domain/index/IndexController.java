package com.prismworks.prism.domain.index;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;

@Hidden
@RestController
@EnableWebMvc
public class IndexController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() {
        return "hello world!";
    }

    @GetMapping(value = "/healthcheck")
    public String healthcheck() {
        return "test8";
    }

    @GetMapping(value = "/secured-uri")
    public ApiSuccessResponse testSecuredUri(@CurrentUser UserContext userContext) {
        return ApiSuccessResponse.defaultOk(Map.of("email", userContext.getEmail()));
    }
}
