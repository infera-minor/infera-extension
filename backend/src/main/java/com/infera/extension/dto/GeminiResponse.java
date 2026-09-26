package com.infera.extension.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/** Minimal representation of the Gemini Generate Content response. */
public class GeminiResponse {
    /** Candidate completions returned by Gemini, in preference order. */
    private List<Candidates> candidates;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    /** One generated candidate and its response content. */
    public static class Candidates {
        private Content content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    /** Gemini response content, composed of one or more parts. */
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    /** A single generated text part. */
    public static class Part {
        private String text;
    }
}
