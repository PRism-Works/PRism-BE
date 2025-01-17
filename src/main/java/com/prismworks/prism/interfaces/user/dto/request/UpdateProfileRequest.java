package com.prismworks.prism.interfaces.user.dto.request;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @Size(min = 1, max = 20)
    private String username;

    @Size(max = 10)
    private List<String> skills;

    @Size(max = 10)
    private List<String> interestJobs;

    @Size(max = 100)
    private String introduction;
}
