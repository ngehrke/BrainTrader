package com.braintrader.handler;

import com.braintrader.exceptions.BrainHistoricalDataHandlerException;
import com.ib.controller.ApiController;
import com.ib.controller.Bar;
import lombok.Getter;

@Getter
class BrainHistoricalDataHandler implements ApiController.IHistoricalDataHandler {

    private final ContractTimeSeries contractTimeSeries;

    public BrainHistoricalDataHandler(ContractTimeSeries contractTimeSeries) throws BrainHistoricalDataHandlerException {

        if (contractTimeSeries==null) {
            throw new BrainHistoricalDataHandlerException("contractTimeSeries is null");
        }

        this.contractTimeSeries = contractTimeSeries;

    }

    @Override
    public void historicalData(Bar bar) {

        DataPoint dataPoint = new DataPoint();

        dataPoint.setClose(bar.close());
        dataPoint.setHigh(bar.high());
        dataPoint.setLow(bar.low());
        dataPoint.setOpen(bar.open());
        dataPoint.setTime(bar.time());
        dataPoint.setFormattedTime(bar.formattedTime());
        dataPoint.setTimeStr(bar.timeStr());
        dataPoint.setVolume(bar.volume());
        dataPoint.setWap(bar.wap());
        dataPoint.setCount(bar.count());

        contractTimeSeries.addDataPoint(dataPoint);

    }

    @Override
    public void historicalDataEnd() {
        // nothing to do
    }

}
