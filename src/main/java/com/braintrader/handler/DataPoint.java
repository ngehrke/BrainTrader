package com.braintrader.handler;

import com.ib.client.Decimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataPoint {

    private double close;
    private double low;
    private double high;
    private double open;
    private long time;
    private String formattedTime;
    private String timeStr;
    private Decimal volume;
    private Decimal wap;
    private int count;

    @Override
    public String toString() {

        return "DataPoint{" +
                "close=" + close +
                ", low=" + low +
                ", high=" + high +
                ", open=" + open +
                ", time=" + time +
                ", formattedTime='" + formattedTime + '\'' +
                ", timeStr='" + timeStr + '\'' +
                ", volume=" + volume +
                ", wap=" + wap +
                ", count=" + count +
                '}';
    }

}
