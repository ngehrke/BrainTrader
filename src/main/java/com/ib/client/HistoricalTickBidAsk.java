/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.client;

import com.ib.client.Decimal;
import com.ib.client.TickAttribBidAsk;

public class HistoricalTickBidAsk {
    private long m_time;
    private com.ib.client.TickAttribBidAsk m_tickAttribBidAsk;
    private double m_priceBid;
    private double m_priceAsk;
    private com.ib.client.Decimal m_sizeBid;
    private com.ib.client.Decimal m_sizeAsk;

    public HistoricalTickBidAsk(long time, com.ib.client.TickAttribBidAsk tickAttribBidAsk, double priceBid, double priceAsk, com.ib.client.Decimal sizeBid, com.ib.client.Decimal sizeAsk) {
        m_time = time;
        m_tickAttribBidAsk = tickAttribBidAsk;
        m_priceBid = priceBid;
        m_priceAsk = priceAsk;
        m_sizeBid = sizeBid;
        m_sizeAsk = sizeAsk;
    }

    public long time() {
        return m_time;
    }

    public TickAttribBidAsk tickAttribBidAsk() {
        return m_tickAttribBidAsk;
    }

    public double priceBid() {
        return m_priceBid;
    }

    public double priceAsk() {
        return m_priceAsk;
    }

    public com.ib.client.Decimal sizeBid() {
        return m_sizeBid;
    }

    public Decimal sizeAsk() {
        return m_sizeAsk;
    }

}