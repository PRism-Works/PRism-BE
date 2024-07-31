package com.prismworks.prism.domain.peerreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortAnswerResponse extends ReviewResponse { // 13번, 14번 응답
    private String description; // 강점을 알려주세요 (50자)
    private String example; // 자세한 예를 들어 설명해주세요 (200자)
}
