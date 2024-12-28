package com.braintrader.porter;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Force {

    private String forceName;
    private List<SecurityInForce> securityInForceList=new ArrayList<SecurityInForce>();

}
