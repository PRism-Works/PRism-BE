package com.prismworks.prism.domain.post.application.dto.command;

import lombok.Builder;
import lombok.Getter;

public class PostCommand {
    @Builder
    @Getter
    public static class CreatePost {
        private String userId;
        private String title;
        private String content;
    }
}
