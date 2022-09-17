package com.backcountry.store.orchestrator.processor;

import com.backcountry.store.orchestrator.dto.PosLogInput;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Base64;

public class InputDataProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        PosLogInput posLog = exchange.getIn().getBody(PosLogInput.class);
        exchange.setProperty("OriginalMessage", posLog);

        String decodedStr  = new String(Base64.getDecoder().decode(posLog.getBody()));
        posLog.setBody(decodedStr);

        exchange.getIn().getHeaders().put("transactionType", posLog.getTransactionType());
        exchange.getIn().getHeaders().put("transactionId", posLog.getTransactionId());
        exchange.getIn().setBody(posLog.getBody());
    }
}