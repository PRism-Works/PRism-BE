package com.prismworks.prism.domain.prism.controller;

import com.prismworks.prism.domain.prism.dto.PrismDataDto;
import com.prismworks.prism.domain.prism.service.PrismService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prism")
public class PrismController {

    private final PrismService prismService;

    public PrismController(PrismService prismService) {
        this.prismService = prismService;
    }

    @GetMapping("/{userId}")
    public PrismDataDto getUserPrismData(@PathVariable String userId) {
        return prismService.calculateUserPrismData(userId);
    }

    @GetMapping("/{userId}/{projectId}")
    public PrismDataDto getUserProjectPrismData(@PathVariable String userId, @PathVariable Integer projectId) {
        return prismService.calculateUserProjectPrismData(userId, projectId);
    }
}
