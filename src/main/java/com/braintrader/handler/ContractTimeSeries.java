package com.braintrader.handler;

import com.braintrader.exceptions.BrainHistoricalDataHandlerException;
import com.ib.client.Contract;
import com.ib.client.Types;
import com.ib.controller.ApiController;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ContractTimeSeries {

    private final ApiController apiController;

    @Getter
    private final Contract contract;

    @Getter
    private List<DataPoint> dataPointList = new ArrayList<>();

    public ContractTimeSeries(ApiController apiController, Contract contract) {

        this.apiController = apiController;
        this.contract = contract;

    }

    public void requestHistoricalData(String endDateTime,int duration,Types. DurationUnit durationUnit, Types. BarSize barSize, Types. WhatToShow whatToShow) throws BrainHistoricalDataHandlerException {

        BrainHistoricalDataHandler brainHistoricalDataHandler = new BrainHistoricalDataHandler(this);
        this.apiController.reqHistoricalData(this.contract, endDateTime, duration, durationUnit, barSize , whatToShow, true,false, brainHistoricalDataHandler);

    }

    protected void addDataPoint(DataPoint dataPoint) {
        dataPointList.add(dataPoint);
    }


}
