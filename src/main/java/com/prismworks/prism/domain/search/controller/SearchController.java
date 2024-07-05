package com.prismworks.prism.domain.search.controller;

import com.prismworks.prism.domain.search.comn.contant.CommonConstant;
import com.prismworks.prism.domain.search.comn.contant.CommonConstant.CommonCode;
import com.prismworks.prism.domain.search.comn.contant.CommonConstant.CommonMessage;
import com.prismworks.prism.domain.search.comn.contant.Path;
import com.prismworks.prism.domain.search.comn.exception.AccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchController {

    @GetMapping(Path.SEARCH_USER_PROFILE + "/{userId}")
    public ResponseEntity<Map<String, Object>> searchUserProfile(@PathVariable String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new AccessDeniedException("Invalid user ID");
        }

        Map<String, String> userProfile = new HashMap<>();
        userProfile.put("name", "John Doe");
        userProfile.put("email", "john@example.com");

        //return ResponseEntity.ok(constructResponse(CommonCode.SUCCESS, CommonMessage.SUCCESS, userProfile));
        return ResponseEntity.ok(constructResponse(CommonCode.FAIL, CommonMessage.FAIL, userProfile));
    }

    @GetMapping(Path.SEARCH_PROJECT + "/{userId}")
    public ResponseEntity<Map<String, Object>> searchUserProjects(@PathVariable String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new AccessDeniedException("Invalid user ID");
        }

        Map<String, String> projectDetails = new HashMap<>();
        projectDetails.put("projectId", "12345");
        projectDetails.put("projectName", "New Horizon");

        return ResponseEntity.ok(constructResponse(CommonCode.SUCCESS, CommonMessage.SUCCESS, projectDetails));
    }

    private Map<String, Object> constructResponse(CommonCode resultCode, CommonMessage resultMessage, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put(CommonConstant.CommonKey.RESULT_CODE.getKey(), resultCode.getValue());
        response.put(CommonConstant.CommonKey.RESULT_MESSAGE.getKey(), resultMessage.getValue());
        response.put(CommonConstant.CommonKey.RESULT_DATA.getKey(), data);
        return response;
    }
}

