package com.prismworks.prism.domain.user.dto.command;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateProfileCommand {
    private final String userId;
    private final String username;
    private final List<String> skills;
    private final List<String> interestJobs;
    private final String introduction;
    private final LocalDateTime requestAt;
}

