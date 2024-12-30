package com.prismworks.prism.domain.prism.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.prism.dto.PrismDataDto;
import com.prismworks.prism.domain.prism.service.PrismService;
import com.prismworks.prism.domain.project.dto.ProjectResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prism")
public class PrismController implements PrismControllerDocs {

    private final PrismService prismService;

    public PrismController(PrismService prismService) {
        this.prismService = prismService;
    }

    @GetMapping("/{userId}")
    public ApiSuccessResponse getUserPrismData(@PathVariable String userId, @RequestParam("prismType") String prismType) {
        PrismDataDto prismDataDto = prismService.calculateUserPrismData(userId, prismType);
        return new ApiSuccessResponse(HttpStatus.OK.value(), prismDataDto);
    }

    @GetMapping("/{userId}/{projectId}")
    public ApiSuccessResponse getUserProjectPrismData(@PathVariable String userId, @PathVariable Integer projectId,
                                                      @RequestParam("prismType") String prismType) {
        PrismDataDto prismDataDto = prismService.calculateUserProjectPrismData(userId, projectId, prismType);
        return new ApiSuccessResponse(HttpStatus.OK.value(), prismDataDto);
    }
}
