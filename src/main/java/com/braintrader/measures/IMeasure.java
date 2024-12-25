package com.braintrader.measures;

import java.time.LocalDate;

public interface IMeasure {

    String getMeasureName();
    LocalDate getMeasureDate();
    Double getMeasureValue();
    String getSymbol();

}
