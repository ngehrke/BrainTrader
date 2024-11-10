/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.client;

import com.ib.client.ContractCondition;
import com.ib.client.ContractLookuper;
import com.ib.client.OrderConditionType;
import com.ib.client.Util;

public class PercentChangeCondition extends ContractCondition {

	public static final com.ib.client.OrderConditionType conditionType = OrderConditionType.PercentChange;
	
	protected PercentChangeCondition() { }
	
	@Override
	public String toString(ContractLookuper lookuper) {
		return super.toString(lookuper);
	}

	@Override
	public String toString() {
		return toString(null);
	}

	private double m_changePercent = Double.MAX_VALUE;

	public double changePercent() {
		return m_changePercent;
	}

	public void changePercent(double m_changePercent) {
		this.m_changePercent = m_changePercent;
	}

	@Override
	protected String valueToString() {
		return Util.DoubleMaxString(m_changePercent);
	}

	@Override
	protected void valueFromString(String v) {
		m_changePercent = Double.parseDouble(v);
	} 
	
}