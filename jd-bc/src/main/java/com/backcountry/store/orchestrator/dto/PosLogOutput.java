package com.backcountry.store.orchestrator.dto;

import com.backcountry.store.orchestrator.models.POSLogType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PosLogOutput {
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("messageFormat")
    private String messageFormat;
    @JsonProperty("publishTimestamp")
    private String publishTimestamp;
    @JsonProperty("body")
    private POSLogType body;
}

