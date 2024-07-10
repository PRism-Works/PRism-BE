package com.prismworks.prism.common.resolver;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.domain.auth.model.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class UserContextArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCurrentUserAnnotation = parameter.getParameterAnnotation(CurrentUser.class) != null;
        boolean isUserContextClass = UserContext.class.equals(parameter.getParameterType());
        return hasCurrentUserAnnotation && isUserContextClass;
    }

    @Override
    public UserContext resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserContext)authentication.getPrincipal();
    }
}
