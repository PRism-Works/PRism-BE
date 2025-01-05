package com.prismworks.prism.common.converter;

import com.prismworks.prism.domain.post.application.dto.RecruitmentPostSortOption;
import com.prismworks.prism.domain.post.domain.model.ProcessMethod;
import com.prismworks.prism.domain.post.domain.model.RecruitmentPosition;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ParamStringToEnumConverter {

    public static class ProcessMethodConverter implements Converter<String, ProcessMethod> {
        @Override
        public ProcessMethod convert(String source) {
            return ProcessMethod.from(source);
        }
    }

    public static class RecruitmentPositionConverter implements Converter<String, RecruitmentPosition> {
        @Override
        public RecruitmentPosition convert(String source) {
            return RecruitmentPosition.from(source);
        }
    }

    public static class RecruitmentPostSortConverter implements Converter<String, RecruitmentPostSortOption> {
        @Override
        public RecruitmentPostSortOption convert(String source) {
            return RecruitmentPostSortOption.from(source);
        }
    }
}
