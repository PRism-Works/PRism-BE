package com.prismworks.prism.domain.peerreview.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/ai",produces = "application/json; charset=utf8")
public class OpenAIController { // todo: 제거

    private final ChatClient chatClient;

    @GetMapping("/call")
    public String testAI(@RequestParam String message) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        ChatResponseMetadata metadata = chatResponse.getMetadata();
        log.info("## model: {}", metadata.getModel());
        return chatResponse.getResult().getOutput().getContent();
    }
}