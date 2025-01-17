package com.prismworks.prism.interfaces.user.mapper;


import com.prismworks.prism.domain.user.dto.command.UpdateProfileCommand;
import com.prismworks.prism.interfaces.user.dto.request.UpdateProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class UserApiMapper {

    public UpdateProfileCommand fromUpdateProfileRequest(UpdateProfileRequest request,
        String userId
    ) {
        return UpdateProfileCommand.builder()
            .userId(userId)
            .username(request.getUsername())
            .skills(request.getSkills())
            .interestJobs(request.getInterestJobs())
            .introduction(request.getIntroduction())
            .build();
    }
}
