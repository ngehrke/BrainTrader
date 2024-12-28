package com.braintrader.porter;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FiveForcesAnalysis {

    private String companyName;
    private String tickerSymbol;

    private List<Force> forceList=new ArrayList<>();

}
