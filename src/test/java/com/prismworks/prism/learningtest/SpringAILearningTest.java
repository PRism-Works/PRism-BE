package com.prismworks.prism.learningtest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class SpringAILearningTest {

    @Autowired
    private ChatClient.Builder clientBuilder;

    @Autowired
    private ChatModel chatModel;

    @Value("${spring.ai.openai.api-key}")
    public String openaiApiKey;

    @DisplayName("spring ai auto configuration으로 생성된 객체들 검증")
    @Test
    public void checkAutoConfiguration() {
        assertThat(clientBuilder).isNotNull();
        assertThat(chatModel).isNotNull();

        ChatClient clientByAutoConfigured = clientBuilder.build();
        ChatClient clientWithAutoConfiguredChatModel = ChatClient.create(chatModel);

        assertThat(clientByAutoConfigured).isNotNull();
        assertThat(clientWithAutoConfiguredChatModel).isNotNull();
    }

    @DisplayName("model을 지정하여 chat client 생성")
    @Test
    public void clientByCustomChatModel() {
        // given, when
        OpenAiApi openAiApi = new OpenAiApi(openaiApiKey);
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                .build();

        ChatModel chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions);
        ChatClient chatClient = ChatClient.create(chatModel);

        // then
        assertThat(chatModel).isNotNull();
        assertThat(chatClient).isNotNull();
    }

    @DisplayName("default system prompt를 정의하여 chat client 생성")
    @Test
    public void createChatClientWithDefaultSystem() {
        // given
        ChatClient chatClientWithDefaultSystem =
                clientBuilder.defaultSystem("Always say hello as your first word")
                        .build();

        ChatClient chatClientWithDefaultSystemParam =
                clientBuilder
                        .defaultSystem("You are a friendly chat bot that answers question in the voice of a {voice}")
                        .build();

        // when
        String contentByDefaultSystem = chatClientWithDefaultSystem.prompt()
                .user("hello?")
                .call()
                .content();

        String contentByDefaultSystemParam = chatClientWithDefaultSystemParam.prompt()
                .system(sp -> sp.param("voice", "kindly"))
                .user("hello?")
                .call()
                .content();

        // then
        log.info("## contentByDefaultSystem: {}", contentByDefaultSystem);
        log.info("## contentByDefaultSystemParam: {}", contentByDefaultSystemParam);
        assertThat(contentByDefaultSystem)
                .isNotEmpty();
        assertThat(contentByDefaultSystemParam)
                .isNotEmpty();
    }

    @DisplayName("custom chat options을 통해 chat completions")
    @Test
    public void chatCompletionsWithChatOptions() {
        // given
        ChatClient chatClient = clientBuilder.build();
        OpenAiChatOptions chatOptions = this.getDefaultChatOptions();

        // when
        String content = chatClient.prompt()
                .options(chatOptions)
                .call()
                .content();

        // then
        log.info("## content: {}", content);
        assertThat(content).isNotEmpty();
    }

    @DisplayName("spring ai prompt객체를 생성하여 chat completions")
    @Test
    public void chatCompletionsWithPrompt() {
        // given
        ChatClient chatClient = clientBuilder.build();

        SystemMessage systemMessage = new SystemMessage("Always say hello as your first word");
        UserMessage userMessage = new UserMessage("hello?");
        List<Message> messages = List.of(systemMessage, userMessage);
        OpenAiChatOptions chatOptions = this.getDefaultChatOptions();

        Prompt prompt = new Prompt(messages, chatOptions);

        // when
        String content = chatClient.prompt(prompt)
                .call()
                .content();

        // then
        log.info("## content: {}", content);
        assertThat(content).isNotEmpty();
    }

    @DisplayName("chat client의 응답으로 spring ai chat response객체 반환")
    @Test
    public void getChatResponse() {
        // given
        ChatClient chatClient = clientBuilder.build();

        // when
        ChatResponse chatResponse = chatClient.prompt()
                .user("hello?")
                .call()
                .chatResponse();

        // then
        assertThat(chatResponse).isNotNull();

        ChatResponseMetadata metadata = chatResponse.getMetadata();
        Generation generation = chatResponse.getResult();
        ChatGenerationMetadata chatGenerationMetadata = generation.getMetadata();
        AssistantMessage assistantMessage = generation.getOutput();
//        List<Generation> results = chatResponse.getResults(); // when chat options n is over 1

        log.info("## metadata: {}", metadata.toString());
        log.info("## generation: {}", generation);
        log.info("## chatGenerationMetadata: {}", chatGenerationMetadata.toString());
        log.info("## assistantMessageContent: {}", assistantMessage.getContent());
    }

    @DisplayName("chat client의 streaming response")
    @Test
    public void getChatStreamResponse() throws InterruptedException {
        // given
        ChatClient chatClient = clientBuilder.build();


        // when
        Flux<ChatResponse> chatResponseFlux = chatClient.prompt()
                .user("hello?")
                .stream()
                .chatResponse();

        // then
        chatResponseFlux.map(chatResponse -> {
            Generation generation = chatResponse.getResult();
            AssistantMessage output = generation.getOutput();
            ChatGenerationMetadata metadata = generation.getMetadata();

            String content = output.getContent();
            String finishReason = metadata.getFinishReason();

            if(StringUtils.hasText(finishReason)) {
                return String.format("finish reason: %s", finishReason);
            }

            if(!StringUtils.hasText(content)) {
                return "";
            }

            return content;
        }).subscribe(
                log::info,
                error-> log.info("", error),
                () -> log.info("end")
        );

        Thread.sleep(10 * 1000L);
    }

    @DisplayName("chat client response 객체로 변환")
    @Test
    public void getEntityResponse() {
        // given, when
        ChatClient chatClient = clientBuilder.build();
        List<ActorFilms> actorFilms = chatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });

        // then
        assertThat(actorFilms)
                .isNotEmpty()
                .hasSize(2);

        assertThat(actorFilms.get(0).getMovies())
                .isNotEmpty()
                .hasSize(5);
        log.info("### actorFilms: {}", actorFilms);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActorFilms {
        private String actor;
        private List<String> movies;
    }

    private OpenAiChatOptions getDefaultChatOptions() {
        return OpenAiChatOptions.builder()
                .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                .withFrequencyPenalty(0.6F)
                .withPresencePenalty(0.6F)
                .withMaxTokens(100)
                .withTemperature(0.6F)
                .build();
    }
}
