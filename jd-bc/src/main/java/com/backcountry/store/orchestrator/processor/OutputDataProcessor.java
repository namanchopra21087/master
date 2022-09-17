package com.backcountry.store.orchestrator.processor;

import com.backcountry.store.orchestrator.dto.PosLogInput;
import com.backcountry.store.orchestrator.dto.PosLogOutput;
import com.backcountry.store.orchestrator.models.POSLogType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

public class OutputDataProcessor implements Processor {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public void process(Exchange exchange) throws Exception {
        PosLogInput originalMessage = exchange.getProperty("OriginalMessage", PosLogInput.class);
        PosLogOutput newMessage = new PosLogOutput();
        POSLogType newMessageBody = exchange.getIn().getBody(POSLogType.class);

        newMessage.setTransactionType(originalMessage.getTransactionType());
        newMessage.setTransactionId(originalMessage.getTransactionId());
        newMessage.setPublishTimestamp(originalMessage.getPublishTimestamp());
        newMessage.setMessageFormat("JSON");
        newMessage.setBody(newMessageBody);

        SimpleDateFormat dataDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(dataDateFormat);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        String jsonData = objectMapper.writeValueAsString(newMessage);

        producerTemplate.sendBodyAndHeaders("direct:publishToTopic", jsonData, exchange.getIn().getHeaders());
    }

    public void setProducerTemplate(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }
}