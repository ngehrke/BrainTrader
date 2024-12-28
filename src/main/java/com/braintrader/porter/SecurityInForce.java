package com.braintrader.porter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityInForce {

    private String securityName;
    private String tickerSymbol;
    private String isin;
    private String reasonForBeingInForce;

}
