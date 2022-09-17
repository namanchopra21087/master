package com.backcountry.store.orchestrator.processor;

import com.backcountry.store.orchestrator.dto.PosLogInput;
import com.backcountry.store.orchestrator.models.POSLogType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FilterDataProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        POSLogType messageBody = exchange.getIn().getBody(POSLogType.class);
        
        String ignoringReason = null;

        if(messageBody.getTransaction().isCancelFlag()) {
            ignoringReason = "cancelled transaction";
        }
        
        if(messageBody.getTransaction().isTrainingModeFlag()){
            ignoringReason = "training transaction";
        }

        if(messageBody.getTransaction() != null &&
                messageBody.getTransaction().getRetailTransaction() != null &&
                StringUtils.equals(messageBody.getTransaction().getRetailTransaction().getTransactionStatus(), "Suspended")) {
            ignoringReason = "suspended transaction";
        }

        if(StringUtils.isNotBlank(ignoringReason)) {
            exchange.getIn().getHeaders().put("ignoreReason", ignoringReason);
        }
    }
}