package com.infera.extension.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/** Request payload accepted by the research endpoint. */
public class ResearchRequest {
    /** Text selected by the user in the active browser tab. */
    private String content;
    /** Requested action, currently {@code summarize} or {@code suggest}. */
    private String operation;

}
