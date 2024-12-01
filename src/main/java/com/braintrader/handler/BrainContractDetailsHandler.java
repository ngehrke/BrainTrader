package com.braintrader.handler;

import com.ib.client.ContractDetails;
import com.ib.controller.ApiController;

import java.util.List;

public class BrainContractDetailsHandler implements ApiController.IContractDetailsHandler {

    @Override
    public void contractDetails(List<ContractDetails> list) {

        for(ContractDetails contractDetails : list) {
            System.out.println("Tradinghours ContractDetails: "+contractDetails.tradingHours());
        }

    }

}
