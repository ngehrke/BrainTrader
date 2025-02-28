/* Copyright (C) 2024 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.client;

import com.ib.client.OrderCondition;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public abstract class OperatorCondition extends OrderCondition {
	
    private static final String HEADER = SPACE + "is" + SPACE;
    private static final String IS_MORE = ">=";
    private static final String IS_LESS = "<=";
	
	private boolean m_isMore;

	protected abstract String valueToString();
	protected abstract void valueFromString(String v);
	
	
	@Override
	public void readFrom(ObjectInput in) throws IOException {
		super.readFrom(in);
		
		m_isMore = in.readBoolean();
		
		valueFromString(in.readUTF());
	}
	
	@Override
	public String toString() {
		return HEADER + (isMore() ? IS_MORE + SPACE : IS_LESS + SPACE) + valueToString() + super.toString();
	}
	
	@Override
	public void writeTo(ObjectOutput out) throws IOException {
		super.writeTo(out);
		out.writeBoolean(m_isMore);
		out.writeUTF(valueToString());
	}
	
	public boolean isMore() {
		return m_isMore;
	}

	public void isMore(boolean m_isMore) {
		this.m_isMore = m_isMore;
	}
	
    @Override public boolean tryToParse(String conditionStr)
    {
        if (!conditionStr.startsWith(HEADER)) {
            return false;
        }

        try
        {
            conditionStr = conditionStr.replace(HEADER, EMPTY);
            if (!conditionStr.startsWith(IS_MORE) && !conditionStr.startsWith(IS_LESS)) {
                return false;
            }
            m_isMore = conditionStr.startsWith(IS_MORE);
            if (super.tryToParse(conditionStr.substring(conditionStr.lastIndexOf(SPACE)))) {
            	conditionStr = conditionStr.substring(0, conditionStr.lastIndexOf(SPACE));
            }
            valueFromString(conditionStr.substring(3));
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }
}