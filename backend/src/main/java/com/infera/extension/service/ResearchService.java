package com.infera.extension.service;

import com.infera.extension.dto.GeminiResponse;
import com.infera.extension.dto.ResearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
/** Builds research prompts, calls Gemini, and extracts generated text. */
public class ResearchService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public ResearchService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper
    ) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    /**
     * Converts an extension request to Gemini's request format and returns its
     * generated text.
     *
     * @param request selected text and requested research operation
     * @return the first generated text response
     * @throws ResponseStatusException when Gemini cannot be reached
     */
    public String processRequest(ResearchRequest request) {

        String prompt = createPrompt(request);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        String response;
        try {
            response = webClient.post()
                    .uri(geminiApiUrl + geminiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientRequestException exception) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Unable to connect to the Gemini API. Check your network connection and try again.",
                    exception
            );
        }

        return extractTextFromResponse(response);
    }

    private String createPrompt(ResearchRequest request) {

        // Each supported operation has a deliberately constrained prompt.

        StringBuilder prompt = new StringBuilder();

        switch (request.getOperation()) {

            case "summarize":
                prompt.append("""
                        Provide a clear and concise summary
                        of the following text in a few sentences:

                        """);
                break;

            case "suggest":
                prompt.append("""
                        Based on the following content, suggest
                        related topics and further reading.

                        Format the response with clear headings
                        and bullet points:

                        """);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown operation: " + request.getOperation()
                );
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("Content must not be blank");
        }

        prompt.append(request.getContent());

        return prompt.toString();
    }

    private String extractTextFromResponse(String response) {

        // Gemini may return multiple candidates; this extension displays the first.

        try {

            GeminiResponse geminiResponse =
                    objectMapper.readValue(response, GeminiResponse.class);

            if (geminiResponse.getCandidates() != null
                    && !geminiResponse.getCandidates().isEmpty()) {

                GeminiResponse.Candidates firstCandidate =
                        geminiResponse.getCandidates().getFirst();

                if (firstCandidate.getContent() != null
                        && firstCandidate.getContent().getParts() != null
                        && !firstCandidate.getContent().getParts().isEmpty()) {

                    return firstCandidate
                            .getContent()
                            .getParts()
                            .get(0)
                            .getText();
                }
            }

            return "No content found";

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse Gemini response", e
            );
        }
    }
}
