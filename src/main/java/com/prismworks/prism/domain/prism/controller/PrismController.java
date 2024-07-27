package com.prismworks.prism.domain.prism.controller;

import com.prismworks.prism.domain.prism.dto.PrismDataDto;
import com.prismworks.prism.domain.prism.service.PrismService;
import jakarta.ws.rs.QueryParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prism")
public class PrismController {

    private final PrismService prismService;

    public PrismController(PrismService prismService) {
        this.prismService = prismService;
    }

    @GetMapping("/{userId}")
    public PrismDataDto getUserPrismData(@PathVariable String userId,@QueryParam("prismType") String prismType) {
        return prismService.calculateUserPrismData(userId, prismType);
    }

    @GetMapping("/{userId}/{projectId}")
    public PrismDataDto getUserProjectPrismData(@PathVariable String userId, @PathVariable Integer projectId,@QueryParam("prismType") String prismType) {
        return prismService.calculateUserProjectPrismData(userId, projectId, prismType);
    }
}
