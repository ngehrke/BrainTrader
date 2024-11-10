/* Copyright (C) 2024 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.client;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ib.client.ComboLeg;
import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDescription;
import com.ib.client.ContractDetails;
import com.ib.client.Decimal;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.DepthMktDataDescription;
import com.ib.client.Execution;
import com.ib.client.FamilyCode;
import com.ib.client.HistogramEntry;
import com.ib.client.HistoricalSession;
import com.ib.client.IneligibilityReason;
import com.ib.client.MarketDataType;
import com.ib.client.NewsProvider;
import com.ib.client.Order;
import com.ib.client.OrderComboLeg;
import com.ib.client.OrderCondition;
import com.ib.client.OrderState;
import com.ib.client.PriceIncrement;
import com.ib.client.SoftDollarTier;
import com.ib.client.TagValue;
import com.ib.client.TickAttrib;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.TickType;
import com.ib.client.Types;
import com.ib.client.Types.SecType;
import com.ib.client.Util;

public class EWrapperMsgGenerator {
    public static final String SCANNER_PARAMETERS = "SCANNER PARAMETERS:";
    public static final String FINANCIAL_ADVISOR = "FA:";
    
	public static String tickPrice( int tickerId, int field, double price, TickAttrib attribs) {
    	return "id=" + tickerId + "  " + com.ib.client.TickType.getField( field) + "=" + com.ib.client.Util.DoubleMaxString(price) + " " +
        (attribs.canAutoExecute() ? " canAutoExecute" : " noAutoExecute") + " pastLimit = " + attribs.pastLimit() +
        (field == com.ib.client.TickType.BID.index() || field == com.ib.client.TickType.ASK.index() ? " preOpen = " + attribs.preOpen() : "");
    }
	
    public static String tickSize( int tickerId, int field, com.ib.client.Decimal size) {
    	return "id=" + tickerId + "  " + com.ib.client.TickType.getField( field) + "=" + size;
    }
    
    public static String tickOptionComputation( int tickerId, int field, int tickAttrib, double impliedVol,
    		double delta, double optPrice, double pvDividend,
    		double gamma, double vega, double theta, double undPrice) {
	return "id=" + tickerId + "  " + com.ib.client.TickType.getField( field) +
            ": tickAttrib = " + com.ib.client.Util.IntMaxString(tickAttrib) +
            " impliedVol = " + com.ib.client.Util.DoubleMaxString(impliedVol, "N/A") +
            " delta = " + com.ib.client.Util.DoubleMaxString(delta, "N/A") +
            " gamma = " + com.ib.client.Util.DoubleMaxString(gamma, "N/A") +
            " vega = " + com.ib.client.Util.DoubleMaxString(vega, "N/A") +
            " theta = " + com.ib.client.Util.DoubleMaxString(theta, "N/A") +
            " optPrice = " + com.ib.client.Util.DoubleMaxString(optPrice, "N/A") +
            " pvDividend = " + com.ib.client.Util.DoubleMaxString(pvDividend, "N/A") +
            " undPrice = " + com.ib.client.Util.DoubleMaxString(undPrice, "N/A");
    }
    
    public static String tickGeneric(int tickerId, int tickType, double value) {
    	return "id=" + tickerId + "  " + com.ib.client.TickType.getField( tickType) + "=" + com.ib.client.Util.DoubleMaxString(value);
    }
    
    public static String tickString(int tickerId, int tickType, String value) {
    	return "id=" + tickerId + "  " + com.ib.client.TickType.getField( tickType) + "=" + value;
    }
    
    public static String tickEFP(int tickerId, int tickType, double basisPoints,
			String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
    	return "id=" + tickerId + "  " + TickType.getField(tickType)
		+ ": basisPoints = " + com.ib.client.Util.DoubleMaxString(basisPoints) + "/" + formattedBasisPoints
		+ " impliedFuture = " + com.ib.client.Util.DoubleMaxString(impliedFuture) + " holdDays = " + com.ib.client.Util.IntMaxString(holdDays) +
		" futureLastTradeDate = " + futureLastTradeDate + " dividendImpact = " + com.ib.client.Util.DoubleMaxString(dividendImpact) +
		" dividends to expiry = "	+ com.ib.client.Util.DoubleMaxString(dividendsToLastTradeDate);
    }
    
    public static String orderStatus(int orderId, String status, com.ib.client.Decimal filled, com.ib.client.Decimal remaining,
                                     double avgFillPrice, int permId, int parentId, double lastFillPrice,
                                     int clientId, String whyHeld, double mktCapPrice) {
    	return "order status: orderId=" + orderId + " clientId=" + com.ib.client.Util.IntMaxString(clientId) + " permId=" + com.ib.client.Util.IntMaxString(permId) +
        " status=" + status + " filled=" + filled + " remaining=" + remaining +
        " avgFillPrice=" + com.ib.client.Util.DoubleMaxString(avgFillPrice) + " lastFillPrice=" + com.ib.client.Util.DoubleMaxString(lastFillPrice) +
        " parent Id=" + com.ib.client.Util.IntMaxString(parentId) + " whyHeld=" + whyHeld + " mktCapPrice=" + com.ib.client.Util.DoubleMaxString(mktCapPrice);
    }
    
    public static String openOrder(int orderId, com.ib.client.Contract contract, com.ib.client.Order order, com.ib.client.OrderState orderState) {
		final StringBuilder sb = new StringBuilder(1024);
        sb.append("open order:");
        appendOrderFields(sb, orderId, contract, order, orderState, true);
        return sb.toString();
    }
    
    public static String openOrderEnd() {
    	return " =============== end ===============";
    }
    
    public static String updateAccountValue(String key, String value, String currency, String accountName) {
    	return "updateAccountValue: " + key + " " + value + " " + currency + " " + accountName;
    }
    
    public static String updatePortfolio(com.ib.client.Contract contract, com.ib.client.Decimal position, double marketPrice,
                                         double marketValue, double averageCost, double unrealizedPNL,
                                         double realizedPNL, String accountName) {
		return "updatePortfolio: "
            + contractMsg(contract)
            + position + " " + com.ib.client.Util.DoubleMaxString(marketPrice) + " " + com.ib.client.Util.DoubleMaxString(marketValue) + " "
            + com.ib.client.Util.DoubleMaxString(averageCost) + " " + com.ib.client.Util.DoubleMaxString(unrealizedPNL) + " "
            + com.ib.client.Util.DoubleMaxString(realizedPNL) + " " + accountName;
    }
    
    public static String updateAccountTime(String timeStamp) {
    	return "updateAccountTime: " + timeStamp;
    }
    
    public static String accountDownloadEnd(String accountName) {
    	return "accountDownloadEnd: " + accountName;
    }
    
    public static String nextValidId( int orderId) {
    	return "Next Valid Order ID: " + orderId;
    }
    
    public static String contractDetails(int reqId, com.ib.client.ContractDetails contractDetails) {
    	com.ib.client.Contract contract = contractDetails.contract();
		return "reqId = " + reqId + " ===================================\n"
            + " ---- Contract Details begin ----\n"
            + contractMsg(contract) + contractDetailsMsg(contractDetails)
            + " ---- Contract Details End ----\n";
    }
    
    private static String contractDetailsMsg(com.ib.client.ContractDetails contractDetails) {
		return "marketName = " + contractDetails.marketName() + "\n"
        + "minTick = " + com.ib.client.Util.DoubleMaxString(contractDetails.minTick()) + "\n"
        + "price magnifier = " + com.ib.client.Util.IntMaxString(contractDetails.priceMagnifier()) + "\n"
        + "orderTypes = " + contractDetails.orderTypes() + "\n"
        + "validExchanges = " + contractDetails.validExchanges() + "\n"
        + "underConId = " + com.ib.client.Util.IntMaxString(contractDetails.underConid()) + "\n"
        + "longName = " + contractDetails.longName() + "\n"
        + "contractMonth = " + contractDetails.contractMonth() + "\n"
        + "industry = " + contractDetails.industry() + "\n"
        + "category = " + contractDetails.category() + "\n"
        + "subcategory = " + contractDetails.subcategory() + "\n"
        + "timeZoneId = " + contractDetails.timeZoneId() + "\n"
        + "tradingHours = " + contractDetails.tradingHours() + "\n"
        + "liquidHours = " + contractDetails.liquidHours() + "\n"
        + "evRule = " + contractDetails.evRule() + "\n"
        + "evMultiplier = " + com.ib.client.Util.DoubleMaxString(contractDetails.evMultiplier()) + "\n"
        + "aggGroup = " + com.ib.client.Util.IntMaxString(contractDetails.aggGroup()) + "\n"
        + "underSymbol = " + contractDetails.underSymbol() + "\n"
        + "underSecType = " + contractDetails.underSecType() + "\n"
        + "marketRuleIds = " + contractDetails.marketRuleIds() + "\n"
        + "realExpirationDate = " + contractDetails.realExpirationDate() + "\n"
        + "lastTradeTime = " + contractDetails.lastTradeTime() + "\n"
        + "stockType = " + contractDetails.stockType() + "\n"
        + "minSize = " + contractDetails.minSize() + "\n"
        + "sizeIncrement = " + contractDetails.sizeIncrement() + "\n"
        + "suggestedSizeIncrement = " + contractDetails.suggestedSizeIncrement() + "\n"
        + contractDetailsFundData(contractDetails)
        + contractDetailsSecIdList(contractDetails) 
        + contractDetailsIneligibilityReasons(contractDetails);
    }

    private static String contractDetailsFundData(com.ib.client.ContractDetails contractDetails) {
        final StringBuilder sb = new StringBuilder();
        if (contractDetails.contract().secType() == SecType.FUND) {
            sb.append("fundData={\n");
            sb.append("  fundName=").append(contractDetails.fundName()).append("\n");
            sb.append("  fundFamily=").append(contractDetails.fundFamily()).append("\n");
            sb.append("  fundType=").append(contractDetails.fundType()).append("\n");
            sb.append("  fundFrontLoad=").append(contractDetails.fundFrontLoad()).append("\n");
            sb.append("  fundBackLoad=").append(contractDetails.fundBackLoad()).append("\n");
            sb.append("  fundBackLoadTimeInterval=").append(contractDetails.fundBackLoadTimeInterval()).append("\n");
            sb.append("  fundManagementFee=").append(contractDetails.fundManagementFee()).append("\n");
            sb.append("  fundClosed=").append(contractDetails.fundClosed()).append("\n");
            sb.append("  fundClosedForNewInvestors=").append(contractDetails.fundClosedForNewInvestors()).append("\n");
            sb.append("  fundClosedForNewMoney=").append(contractDetails.fundClosedForNewMoney()).append("\n");
            sb.append("  fundNotifyAmount=").append(contractDetails.fundNotifyAmount()).append("\n");
            sb.append("  fundMinimumInitialPurchase=").append(contractDetails.fundMinimumInitialPurchase()).append("\n");
            sb.append("  fundSubsequentMinimumPurchase=").append(contractDetails.fundSubsequentMinimumPurchase()).append("\n");
            sb.append("  fundBlueSkyStates=").append(contractDetails.fundBlueSkyStates()).append("\n");
            sb.append("  fundBlueSkyTerritories=").append(contractDetails.fundBlueSkyTerritories()).append("\n");
            if (contractDetails.fundDistributionPolicyIndicator() != null) {
                sb.append("  fundDistributionPolicyIndicator=").append(contractDetails.fundDistributionPolicyIndicator().name()).append("\n");
            }
            if (contractDetails.fundAssetType() != null) {
                sb.append("  fundAssetType=").append(contractDetails.fundAssetType().name()).append("\n");
            }
            sb.append("}\n");
        }
        return sb.toString();
    }

	private static String contractMsg(com.ib.client.Contract contract) {
		return "conid = " + contract.conid() + "\n"
        + "symbol = " + contract.symbol() + "\n"
        + "secType = " + contract.getSecType() + "\n"
        + "lastTradeDateOrContractMonth = " + contract.lastTradeDateOrContractMonth() + "\n"
        + "lastTradeDate = " + contract.lastTradeDate() + "\n"
        + "strike = " + com.ib.client.Util.DoubleMaxString(contract.strike()) + "\n"
        + "right = " + contract.getRight() + "\n"
        + "multiplier = " + contract.multiplier() + "\n"
        + "exchange = " + contract.exchange() + "\n"
        + "primaryExch = " + contract.primaryExch() + "\n"
        + "currency = " + contract.currency() + "\n"
        + "localSymbol = " + contract.localSymbol() + "\n"
        + "tradingClass = " + contract.tradingClass() + "\n";
    }
	
    public static String bondContractDetails(int reqId, com.ib.client.ContractDetails contractDetails) {
        com.ib.client.Contract contract = contractDetails.contract();
		return "reqId = " + reqId + " ===================================\n"	
        + " ---- Bond Contract Details begin ----\n"
        + "symbol = " + contract.symbol() + "\n"
        + "secType = " + contract.getSecType() + "\n"
        + "cusip = " + contractDetails.cusip() + "\n"
        + "coupon = " + com.ib.client.Util.DoubleMaxString(contractDetails.coupon()) + "\n"
        + "maturity = " + contractDetails.maturity() + "\n"
        + "issueDate = " + contractDetails.issueDate() + "\n"
        + "ratings = " + contractDetails.ratings() + "\n"
        + "bondType = " + contractDetails.bondType() + "\n"
        + "couponType = " + contractDetails.couponType() + "\n"
        + "convertible = " + contractDetails.convertible() + "\n"
        + "callable = " + contractDetails.callable() + "\n"
        + "putable = " + contractDetails.putable() + "\n"
        + "descAppend = " + contractDetails.descAppend() + "\n"
        + "exchange = " + contract.exchange() + "\n"
        + "currency = " + contract.currency() + "\n"
        + "marketName = " + contractDetails.marketName() + "\n"
        + "tradingClass = " + contract.tradingClass() + "\n"
        + "conid = " + contract.conid() + "\n"
        + "minTick = " + com.ib.client.Util.DoubleMaxString(contractDetails.minTick()) + "\n"
        + "orderTypes = " + contractDetails.orderTypes() + "\n"
        + "validExchanges = " + contractDetails.validExchanges() + "\n"
        + "nextOptionDate = " + contractDetails.nextOptionDate() + "\n"
        + "nextOptionType = " + contractDetails.nextOptionType() + "\n"
        + "nextOptionPartial = " + contractDetails.nextOptionPartial() + "\n"
        + "notes = " + contractDetails.notes() + "\n"
        + "longName = " + contractDetails.longName() + "\n"
        + "evRule = " + contractDetails.evRule() + "\n"
        + "evMultiplier = " + com.ib.client.Util.DoubleMaxString(contractDetails.evMultiplier()) + "\n"
        + "aggGroup = " + com.ib.client.Util.IntMaxString(contractDetails.aggGroup()) + "\n"
        + "marketRuleIds = " + contractDetails.marketRuleIds() + "\n"
        + "timeZoneId = " + contractDetails.timeZoneId() + "\n"
        + "lastTradeTime = " + contractDetails.lastTradeTime() + "\n"
        + "minSize = " + contractDetails.minSize() + "\n"
        + "sizeIncrement = " + contractDetails.sizeIncrement() + "\n"
        + "suggestedSizeIncrement = " + contractDetails.suggestedSizeIncrement() + "\n"
        + contractDetailsSecIdList(contractDetails)
        + " ---- Bond Contract Details End ----\n";
    }
    
    private static String contractDetailsSecIdList(com.ib.client.ContractDetails contractDetails) {
        final StringBuilder sb = new StringBuilder(32);
        sb.append("secIdList={");
        if (contractDetails.secIdList() != null) {
			for (com.ib.client.TagValue param : contractDetails.secIdList()) {
				sb.append(param.m_tag).append("=").append(param.m_value).append(',');
			}
			if (!contractDetails.secIdList().isEmpty()) {
				sb.setLength(sb.length() - 1);
			}
        }
        sb.append("}\n");
        return sb.toString();
    }
    
    public static String contractDetailsIneligibilityReasons(com.ib.client.ContractDetails contractDetails) {
        final StringBuilder sb = new StringBuilder();
        sb.append("ineligibilityReasonList={");
        sb.append(contractDetailsIneligibilityReasonList(contractDetails));
        sb.append("}\n");
        return sb.toString();
    }

    public static String contractDetailsIneligibilityReasonList(com.ib.client.ContractDetails contractDetails) {
        final StringBuilder sb = new StringBuilder();
        if (contractDetails.ineligibilityReasonList() != null) {
            for (IneligibilityReason ineligibilityReason : contractDetails.ineligibilityReasonList()) {
                sb.append(ineligibilityReason).append(";");
            }
        }
        return sb.toString();
    }
    
    public static String contractDetailsEnd(int reqId) {
    	return "reqId = " + reqId + " =============== end ===============";
    }
    
    public static String execDetails(int reqId, com.ib.client.Contract contract, Execution execution) {
		return " ---- Execution Details begin ----\n"
        + "reqId = " + reqId + "\n"
        + "orderId = " + execution.orderId() + "\n"
        + "clientId = " + com.ib.client.Util.IntMaxString(execution.clientId()) + "\n"
        + contractMsg(contract)
        + "execId = " + execution.execId() + "\n"
        + "time = " + execution.time() + "\n"
        + "acctNumber = " + execution.acctNumber() + "\n"
        + "executionExchange = " + execution.exchange() + "\n"
        + "side = " + execution.side() + "\n"
        + "shares = " + execution.shares() + "\n"
        + "price = " + com.ib.client.Util.DoubleMaxString(execution.price()) + "\n"
        + "permId = " + com.ib.client.Util.IntMaxString(execution.permId()) + "\n"
        + "liquidation = " + com.ib.client.Util.IntMaxString(execution.liquidation()) + "\n"
        + "cumQty = " + execution.cumQty() + "\n"
        + "avgPrice = " + com.ib.client.Util.DoubleMaxString(execution.avgPrice()) + "\n"
        + "orderRef = " + execution.orderRef() + "\n"
        + "evRule = " + execution.evRule() + "\n"
        + "evMultiplier = " + com.ib.client.Util.DoubleMaxString(execution.evMultiplier()) + "\n"
        + "modelCode = " + execution.modelCode() + "\n"
        + "lastLiquidity = " + execution.lastLiquidity() + "\n"
        + "pendingPriceRevision = " + execution.pendingPriceRevision() + "\n"
        + " ---- Execution Details end ----\n";
    }
    
    public static String execDetailsEnd(int reqId) {
    	return "reqId = " + reqId + " =============== end ===============";
    }
    
    public static String updateMktDepth( int tickerId, int position, int operation, int side,
    									 double price, com.ib.client.Decimal size) {
    	return "updateMktDepth: " + tickerId + " " + position + " " + operation + " " + side + " " + com.ib.client.Util.DoubleMaxString(price) + " " + size;
    }
    
    public static String updateMktDepthL2(int tickerId, int position, String marketMaker,
                                          int operation, int side, double price, com.ib.client.Decimal size, boolean isSmartDepth) {
    	return "updateMktDepth: " + tickerId + " " + position + " " + marketMaker + " " + operation + " " + side + " " + com.ib.client.Util.DoubleMaxString(price) + " " + size + " " + isSmartDepth;
    }
    
    public static String updateNewsBulletin( int msgId, int msgType, String message, String origExchange) {
    	return "MsgId=" + msgId + " :: MsgType=" + com.ib.client.Util.IntMaxString(msgType) +  " :: Origin=" + origExchange + " :: Message=" + message;
    }
    
    public static String managedAccounts( String accountsList) {
    	return "Connected : The list of managed accounts are : [" + accountsList + "]";
    }
    
    public static String receiveFA(int faDataType, String xml) {
        return FINANCIAL_ADVISOR + " " + Types.FADataType.getById(faDataType) + " " + xml;
    }
    
    public static String historicalData(int reqId, String date, double open, double high, double low,
                                        double close, com.ib.client.Decimal volume, int count, com.ib.client.Decimal WAP) {
    	return "id=" + reqId +
        " date = " + date +
        " open=" + com.ib.client.Util.DoubleMaxString(open) +
        " high=" + com.ib.client.Util.DoubleMaxString(high) +
        " low=" + com.ib.client.Util.DoubleMaxString(low) +
        " close=" + com.ib.client.Util.DoubleMaxString(close) +
        " volume=" + volume +
        " count=" + com.ib.client.Util.IntMaxString(count) +
        " WAP=" + WAP;
    }
    public static String historicalDataEnd(int reqId, String startDate, String endDate) {
    	return "id=" + reqId +
    			" start date = " + startDate +
    			" end date=" + endDate;
    }
    
	public static String realtimeBar(int reqId, long time, double open,
                                     double high, double low, double close, com.ib.client.Decimal volume, com.ib.client.Decimal wap, int count) {
        return "id=" + reqId +
        " time = " + com.ib.client.Util.LongMaxString(time) +
        " open=" + com.ib.client.Util.DoubleMaxString(open) +
        " high=" + com.ib.client.Util.DoubleMaxString(high) +
        " low=" + com.ib.client.Util.DoubleMaxString(low) +
        " close=" + com.ib.client.Util.DoubleMaxString(close) +
        " volume=" + volume +
        " count=" + com.ib.client.Util.IntMaxString(count) +
        " WAP=" + wap;
	}
	
    public static String scannerParameters(String xml) {
    	return SCANNER_PARAMETERS + "\n" + xml;
    }
    
    public static String scannerData(int reqId, int rank, ContractDetails contractDetails,
    								 String distance, String benchmark, String projection,
    								 String legsStr) {
        com.ib.client.Contract contract = contractDetails.contract();
    	return "id = " + reqId +
        " rank=" + com.ib.client.Util.IntMaxString(rank) +
        " symbol=" + contract.symbol() +
        " secType=" + contract.getSecType() +
        " lastTradeDateOrContractMonth=" + contract.lastTradeDateOrContractMonth() +
        " strike=" + com.ib.client.Util.DoubleMaxString(contract.strike()) +
        " right=" + contract.getRight() +
        " exchange=" + contract.exchange() +
        " currency=" + contract.currency() +
        " localSymbol=" + contract.localSymbol() +
        " marketName=" + contractDetails.marketName() +
        " tradingClass=" + contract.tradingClass() +
        " distance=" + distance +
        " benchmark=" + benchmark +
        " projection=" + projection +
        " legsStr=" + legsStr;
    }
    
    public static String scannerDataEnd(int reqId) {
    	return "id = " + reqId + " =============== end ===============";
    }
    
    public static String currentTime(long time) {
		return "current time = " + time +
		" (" + DateFormat.getDateTimeInstance().format(new Date(time * 1000)) + ")";
    }

    public static String fundamentalData(int reqId, String data) {
		return "id  = " + reqId + " len = " + data.length() + '\n' + data;
    }
    
    public static String deltaNeutralValidation(int reqId, com.ib.client.DeltaNeutralContract deltaNeutralContract) {
    	return "id = " + reqId
    	+ " deltaNeutralContract.conId =" + deltaNeutralContract.conid()
    	+ " deltaNeutralContract.delta =" + com.ib.client.Util.DoubleMaxString(deltaNeutralContract.delta())
    	+ " deltaNeutralContract.price =" + com.ib.client.Util.DoubleMaxString(deltaNeutralContract.price());
    }
    public static String tickSnapshotEnd(int tickerId) {
    	return "id=" + tickerId + " =============== end ===============";
    }
    
    public static String marketDataType(int reqId, int marketDataType){
    	return "id=" + reqId + " marketDataType = " + MarketDataType.getField(marketDataType);
    }
    
    public static String commissionReport( CommissionReport commissionReport) {
		return "commission report:" +
        " execId=" + commissionReport.execId() +
        " commission=" + com.ib.client.Util.DoubleMaxString(commissionReport.commission()) +
        " currency=" + commissionReport.currency() +
        " realizedPNL=" + com.ib.client.Util.DoubleMaxString(commissionReport.realizedPNL()) +
        " yield=" + com.ib.client.Util.DoubleMaxString(commissionReport.yield()) +
        " yieldRedemptionDate=" + com.ib.client.Util.IntMaxString(commissionReport.yieldRedemptionDate());
    }
    
    public static String position(String account, com.ib.client.Contract contract, com.ib.client.Decimal pos, double avgCost) {
		return " ---- Position begin ----\n"
        + "account = " + account + "\n"
        + contractMsg(contract)
        + "position = " + pos + "\n"
        + "avgCost = " + com.ib.client.Util.DoubleMaxString(avgCost) + "\n"
        + " ---- Position end ----\n";
    }    

    public static String positionEnd() {
        return " =============== end ===============";
    }

    public static String accountSummary( int reqId, String account, String tag, String value, String currency) {
		return " ---- Account Summary begin ----\n"
        + "reqId = " + reqId + "\n"
        + "account = " + account + "\n"
        + "tag = " + tag + "\n"
        + "value = " + value + "\n"
        + "currency = " + currency + "\n"
        + " ---- Account Summary end ----\n";
    }

    public static String accountSummaryEnd( int reqId) {
    	return "id=" + reqId + " =============== end ===============";
    }

    public static String positionMulti(int reqId, String account, String modelCode, com.ib.client.Contract contract, com.ib.client.Decimal pos, double avgCost) {
		return " ---- Position begin ----\n"
        + "id = " + reqId + "\n"
        + "account = " + account + "\n"
        + "modelCode = " + modelCode + "\n"
        + contractMsg(contract)
        + "position = " + pos + "\n"
        + "avgCost = " + com.ib.client.Util.DoubleMaxString(avgCost) + "\n"
        + " ---- Position end ----\n";
    }    

    public static String positionMultiEnd( int reqId) {
        return "id = " + reqId + " =============== end ===============";
    }

    public static String accountUpdateMulti( int reqId, String account, String modelCode, String key, String value, String currency) {
		return " id = " + reqId + " account = " + account + " modelCode = " + modelCode + 
                " key = " + key + " value = " + value + " currency = " + currency;
    }

    public static String accountUpdateMultiEnd( int reqId) {
    	return "id = " + reqId + " =============== end ===============";
    }    

	public static String securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass,
			String multiplier, Set<String> expirations, Set<Double> strikes) {
		final StringBuilder sb = new StringBuilder(128);
		sb.append(" id = ").append(reqId)
				.append(" exchange = ").append(exchange)
				.append(" underlyingConId = ").append(underlyingConId)
				.append(" tradingClass = ").append(tradingClass)
				.append(" multiplier = ").append(multiplier)
				.append(" expirations: ");
		for (String expiration : expirations) {
			sb.append(expiration).append(", ");
		}
		sb.append(" strikes: ");
		for (Double strike : strikes) {
			sb.append(com.ib.client.Util.DoubleMaxString(strike)).append(", ");
		}
		return sb.toString();
	}

	public static String securityDefinitionOptionalParameterEnd( int reqId) {
		return "id = " + reqId + " =============== end ===============";
	}

	public static String softDollarTiers(int reqId, com.ib.client.SoftDollarTier[] tiers) {
		StringBuilder sb = new StringBuilder();
		sb.append("==== Soft Dollar Tiers Begin (total=").append(tiers.length).append(") reqId: ").append(reqId).append(" ====\n");
		for (int i = 0; i < tiers.length; i++) {
			sb.append("Soft Dollar Tier [").append(i).append("] - name: ").append(tiers[i].name())
					.append(", value: ").append(tiers[i].value()).append("\n");
		}
		sb.append("==== Soft Dollar Tiers End (total=").append(tiers.length).append(") ====\n");

		return sb.toString();
	}

	public static String familyCodes(FamilyCode[] familyCodes) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("==== Family Codes Begin (total=").append(familyCodes.length).append(") ====\n");
        for (int i = 0; i < familyCodes.length; i++) {
            sb.append("Family Code [").append(i)
					.append("] - accountID: ").append(familyCodes[i].accountID())
					.append(", familyCode: ").append(familyCodes[i].familyCodeStr())
					.append("\n");
        }
        sb.append("==== Family Codes End (total=").append(familyCodes.length).append(") ====\n");

        return sb.toString();
    }

    public static String symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("==== Symbol Samples Begin (total=").append(contractDescriptions.length).append(") reqId: ").append(reqId).append(" ====\n");
        for (int i = 0; i < contractDescriptions.length; i++) {
            sb.append("---- Contract Description Begin (").append(i).append(") ----\n");
            sb.append("conId: ").append(contractDescriptions[i].contract().conid()).append("\n");
            sb.append("symbol: ").append(contractDescriptions[i].contract().symbol()).append("\n");
            sb.append("secType: ").append(contractDescriptions[i].contract().secType()).append("\n");
            sb.append("primaryExch: ").append(contractDescriptions[i].contract().primaryExch()).append("\n");
            sb.append("currency: ").append(contractDescriptions[i].contract().currency()).append("\n");
            sb.append("derivativeSecTypes (total=").append(contractDescriptions[i].derivativeSecTypes().length).append("): ");
            for (int j = 0; j < contractDescriptions[i].derivativeSecTypes().length; j++){
                sb.append(contractDescriptions[i].derivativeSecTypes()[j]).append(' ');
            }
            sb.append("\n");
            sb.append("description: ").append(com.ib.client.Util.NormalizeString(contractDescriptions[i].contract().description())).append("\n");
            sb.append("issuerId: ").append(com.ib.client.Util.NormalizeString(contractDescriptions[i].contract().issuerId())).append("\n");
            sb.append("---- Contract Description End (").append(i).append(") ----\n");
        }
        sb.append("==== Symbol Samples End (total=").append(contractDescriptions.length).append(") reqId: ").append(reqId).append(" ====\n");

        return sb.toString();
    }

	public static String mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
		StringBuilder sb = new StringBuilder();
		sb.append("==== Market Depth Exchanges Begin (total=").append(depthMktDataDescriptions.length).append(") ====\n");
		for (int i = 0; i < depthMktDataDescriptions.length; i++) {
			sb.append("Depth Market Data Description [").append(i).append("] - exchange: ").append(depthMktDataDescriptions[i].exchange())
					.append(", secType: ").append(depthMktDataDescriptions[i].secType())
					.append(", listingExch: ").append(depthMktDataDescriptions[i].listingExch())
					.append(", serviceDataType: ").append(depthMktDataDescriptions[i].serviceDataType())
					.append(", aggGroup: ").append(com.ib.client.Util.IntMaxString(depthMktDataDescriptions[i].aggGroup())).append("\n");
		}
		sb.append("==== Market Depth Exchanges End (total=").append(depthMktDataDescriptions.length).append(") ====\n");
		return sb.toString();
	}

	public static String tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
		return "TickNews. tickerId: " + tickerId + ", timeStamp: " + com.ib.client.Util.UnixMillisecondsToString(timeStamp, "yyyyMMdd-HH:mm:ss") +
				", providerCode: " + providerCode + ", articleId: " + articleId + ", headline: " + headline + ", extraData: " + extraData;
	}

	public static String newsProviders(NewsProvider[] newsProviders) {
		StringBuilder sb = new StringBuilder();
		sb.append("==== News Providers Begin (total=").append(newsProviders.length).append(") ====\n");
		for (int i = 0; i < newsProviders.length; i++) {
			sb.append("News Provider [").append(i).append("] - providerCode: ").append(newsProviders[i].providerCode()).append(", providerName: ")
					.append(newsProviders[i].providerName()).append("\n");
		}
		sb.append("==== News Providers End (total=").append(newsProviders.length).append(") ====\n");

		return sb.toString();
	}

    public static String error( Exception ex) { return "Error - " + ex;}
    public static String error( String str) { return str;}

	public static String error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
		String ret = id + " | " + errorCode + " | " + errorMsg;
		if (advancedOrderRejectJson != null) {
			ret += (" | " + advancedOrderRejectJson);
		}
		return ret;
	}

	public static String connectionClosed() {
		return "Connection Closed";
	}

	public static String softDollarTiers(com.ib.client.SoftDollarTier[] tiers) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("==== Soft Dollar Tiers Begin (total=").append(tiers.length).append(") ====\n");
		
		for (SoftDollarTier tier : tiers) {
			sb.append(tier).append("\n");
		}
		
		sb.append("==== Soft Dollar Tiers End (total=").append(tiers.length).append(") ====\n");
		
		return sb.toString();
	}

	public static String tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
		return "id=" + tickerId + " minTick = " + com.ib.client.Util.DoubleMaxString(minTick) + " bboExchange = " + bboExchange + " snapshotPermissions = " + com.ib.client.Util.IntMaxString(snapshotPermissions);
	}

	public static String smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("==== Smart Components Begin (total=").append(theMap.entrySet().size()).append(") reqId = ").append(reqId).append("====\n");
		
		for (Entry<Integer, Entry<String, Character>> item : theMap.entrySet()) {
			sb.append("bit number: ").append(item.getKey()).append(", exchange: ").append(item.getValue().getKey()).append(", exchange letter: ").append(item.getValue().getValue()).append("\n");
		}
		
		sb.append("==== Smart Components End (total=").append(theMap.entrySet().size()).append(") reqId = ").append(reqId).append("====\n");
		
		return sb.toString();
	}

	public static String newsArticle(int requestId, int articleType, String articleText) {
		StringBuilder sb = new StringBuilder();
		sb.append("==== News Article Begin requestId: ").append(requestId).append(" ====\n");
		if (articleType == 0) {
			sb.append("---- Article type is text or html ----\n");
			sb.append(articleText).append("\n");
		} else if (articleType == 1) {
			sb.append("---- Article type is binary/pdf ----\n");
			sb.append("Binary/pdf article text cannot be displayed\n");
		}
		sb.append("==== News Article End requestId: ").append(requestId).append(" ====\n");
		return sb.toString();
	}
	
	public static String historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
		return "Historical News. RequestId: " + requestId + ", time: " + time + ", providerCode: " + providerCode + 
				", articleId: " + articleId + ", headline: " + headline;
	}

	public static String historicalNewsEnd( int requestId, boolean hasMore) {
		return "Historical News End. RequestId: " + requestId + ", hasMore: " + hasMore;
	}

	public static String headTimestamp(int reqId, String headTimestamp) {		
		return "Head timestamp. Req Id: " + reqId + ", headTimestamp: " + headTimestamp;
	}

	public static String histogramData(int reqId, List<HistogramEntry> items) {
		StringBuilder sb = new StringBuilder();		
		sb.append("Histogram data. Req Id: ").append(reqId).append(", Data (").append(items.size()).append("):\n");		
		items.forEach(i -> sb.append("\tPrice: ").append(com.ib.client.Util.DoubleMaxString(i.price())).append(", Size: ").append(i.size()).append("\n"));
		return sb.toString();
	}
	
	public static String rerouteMktDataReq(int reqId, int conId, String exchange) {
		return "Re-route market data request. Req Id: " + reqId + ", Con Id: " + conId + ", Exchange: " + exchange;
	}

	public static String rerouteMktDepthReq(int reqId, int conId, String exchange) {
		return "Re-route market depth request. Req Id: " + reqId + ", Con Id: " + conId + ", Exchange: " + exchange;
	}
	
	public static String marketRule(int marketRuleId, com.ib.client.PriceIncrement[] priceIncrements) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("==== Market Rule Begin (marketRuleId=").append(marketRuleId).append(") ====\n");
		for (PriceIncrement priceIncrement : priceIncrements) {
			sb.append("Low Edge: ").append(com.ib.client.Util.DoubleMaxString(priceIncrement.lowEdge()));
			sb.append(", Increment: ").append(com.ib.client.Util.DoubleMaxString(priceIncrement.increment()));
			sb.append("\n");
		}
		sb.append("==== Market Rule End (marketRuleId=").append(marketRuleId).append(") ====\n");
		return sb.toString();
	}
	

    public static String pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
		return "Daily PnL. Req Id: " + reqId + ", daily PnL: " + com.ib.client.Util.DoubleMaxString(dailyPnL) + ", unrealizedPnL: " + com.ib.client.Util.DoubleMaxString(unrealizedPnL) + ", realizedPnL: " + com.ib.client.Util.DoubleMaxString(realizedPnL);
    }
    
    public static String pnlSingle(int reqId, com.ib.client.Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
		return "Daily PnL Single. Req Id: " + reqId + ", pos: " + pos + ", daily PnL: " + com.ib.client.Util.DoubleMaxString(dailyPnL) + ", unrealizedPnL: " + com.ib.client.Util.DoubleMaxString(unrealizedPnL) + ", realizedPnL: " + com.ib.client.Util.DoubleMaxString(realizedPnL) + ", value: " + com.ib.client.Util.DoubleMaxString(value);
    }

    public static String historicalTick(int reqId, long time, double price, com.ib.client.Decimal size) {
        return "Historical Tick. Req Id: " + reqId + ", time: " + com.ib.client.Util.UnixSecondsToString(time, "yyyyMMdd-HH:mm:ss") + ", price: " + com.ib.client.Util.DoubleMaxString(price) + ", size: "
                + size;
    }

    public static String historicalTickBidAsk(int reqId, long time, com.ib.client.TickAttribBidAsk tickAttribBidAsk, double priceBid, double priceAsk,
                                              com.ib.client.Decimal sizeBid, com.ib.client.Decimal sizeAsk) {
        return "Historical Tick Bid/Ask. Req Id: " + reqId + ", time: " + com.ib.client.Util.UnixSecondsToString(time, "yyyyMMdd-HH:mm:ss") + ", bid price: " + com.ib.client.Util.DoubleMaxString(priceBid)
                + ", ask price: " + com.ib.client.Util.DoubleMaxString(priceAsk) + ", bid size: " + sizeBid + ", ask size: " + sizeAsk
                + ", tick attribs: " + (tickAttribBidAsk.bidPastLow() ? "bidPastLow " : "") + (tickAttribBidAsk.askPastHigh() ? "askPastHigh " : "");
    }

    public static String historicalTickLast(int reqId, long time, com.ib.client.TickAttribLast tickAttribLast, double price, com.ib.client.Decimal size, String exchange,
                                            String specialConditions) {
        return "Historical Tick Last. Req Id: " + reqId + ", time: " + com.ib.client.Util.UnixSecondsToString(time, "yyyyMMdd-HH:mm:ss") + ", price: " + com.ib.client.Util.DoubleMaxString(price) + ", size: "
                + size + ", exchange: " + exchange + ", special conditions:" + specialConditions 
                + ", tick attribs: " + (tickAttribLast.pastLimit() ? "pastLimit " : "") + (tickAttribLast.unreported() ? "unreported " : "");
    }
    
    public static String tickByTickAllLast(int reqId, int tickType, long time, double price, com.ib.client.Decimal size, TickAttribLast tickAttribLast,
                                           String exchange, String specialConditions){
        return (tickType == 1 ? "Last." : "AllLast.") +
                " Req Id: " + reqId + " Time: " + com.ib.client.Util.UnixSecondsToString(time, "yyyyMMdd-HH:mm:ss") + " Price: " + com.ib.client.Util.DoubleMaxString(price) + " Size: " + size +
                " Exch: " + exchange + " Spec Cond: " + specialConditions + " Tick Attibs: " + (tickAttribLast.pastLimit() ? "pastLimit " : "") +
                (tickType == 1 ? "" : (tickAttribLast.unreported() ? "unreported " : ""));
    }
    
    public static String tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, com.ib.client.Decimal bidSize, Decimal askSize,
                                          TickAttribBidAsk tickAttribBidAsk){
        return "BidAsk. Req Id: " + reqId + " Time: " + com.ib.client.Util.UnixSecondsToString(time, "yyyyMMdd-HH:mm:ss") + " BidPrice: " + com.ib.client.Util.DoubleMaxString(bidPrice) +
                " AskPrice: " + com.ib.client.Util.DoubleMaxString(askPrice) + " BidSize: " + bidSize + " AskSize: " + askSize + " Tick Attibs: " +
                (tickAttribBidAsk.bidPastLow() ? "bidPastLow " : "") + (tickAttribBidAsk.askPastHigh() ? "askPastHigh " : "");
    }

    public static String tickByTickMidPoint(int reqId, long time, double midPoint){
        return "MidPoint. Req Id: " + reqId + " Time: " + com.ib.client.Util.UnixSecondsToString(time, "yyyyMMdd-HH:mm:ss") + " MidPoint: " + com.ib.client.Util.DoubleMaxString(midPoint);
    }
    
    public static String orderBound(long orderId, int apiClientId, int apiOrderId){
        return "order bound: apiOrderId=" + com.ib.client.Util.IntMaxString(apiOrderId) + " apiClientId=" + com.ib.client.Util.IntMaxString(apiClientId) + " permId=" + com.ib.client.Util.LongMaxString(orderId);
    }
    
    public static String completedOrder(com.ib.client.Contract contract, com.ib.client.Order order, com.ib.client.OrderState orderState) {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append("completed order:");
        appendOrderFields(sb, Integer.MAX_VALUE, contract, order, orderState, false);
        return sb.toString();
    }
    
    public static String completedOrdersEnd() {
        return "=============== end ===============";
    }
 
    public static String replaceFAEnd(int reqId, String text) {
    	return "id = " + reqId + " ===== " + text + " =====";
    }
    
    public static String wshMetaData(int reqId, String dataJson) {
    	return "wshMetaData. id = " + reqId + " dataJson= " + dataJson;
    } 
    
    public static String wshEventData(int reqId, String dataJson) {
    	return "wshEventData. id = " + reqId + " dataJson= " + dataJson;
    } 

    public static String historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<com.ib.client.HistoricalSession> sessions) {
        StringBuilder sb = new StringBuilder();
        sb.append("==== Historical Schedule Begin (ReqId=").append(reqId).append(") ====\n");
        sb.append("Start: ").append(startDateTime);
        sb.append(" End: ").append(endDateTime);
        sb.append(" Time Zone: ").append(timeZone);
        sb.append("\n");

        for (HistoricalSession session: sessions) {
            sb.append("Session: ");
            sb.append("Start: ").append(session.startDateTime());
            sb.append(" End: ").append(session.endDateTime());
            sb.append(" Ref Date: ").append(session.refDate());
            sb.append("\n");
        }
        sb.append("==== Historical Schedule End (ReqId=").append(reqId).append(") ====\n");
        return sb.toString();
    }

    public static String userInfo(int reqId, String whiteBrandingId) {
        return "UserInfo. Req Id: " + reqId + " White Branding Id: " + whiteBrandingId;
    } 

    private static void appendOrderFields(StringBuilder sb, int orderId, Contract contract, Order order, OrderState orderState,
                                          boolean isOpenOrder) {
        com.ib.client.Util.appendValidIntValue(sb, "orderId", orderId);
        com.ib.client.Util.appendNonEmptyString(sb, "action", order.getAction());
        com.ib.client.Util.appendNonEmptyString(sb, "quantity", order.totalQuantity().toString());
        com.ib.client.Util.appendPositiveDoubleValue(sb, "cashQty", order.cashQty());
        com.ib.client.Util.appendPositiveIntValue(sb, "conid", contract.conid());
        com.ib.client.Util.appendNonEmptyString(sb, "symbol", contract.symbol());
        com.ib.client.Util.appendNonEmptyString(sb, "secType", contract.getSecType());
        com.ib.client.Util.appendNonEmptyString(sb, "lastTradeDateOrContractMonth", contract.lastTradeDateOrContractMonth());
        com.ib.client.Util.appendPositiveDoubleValue(sb, "strike", contract.strike());
        com.ib.client.Util.appendNonEmptyString(sb, "right", contract.getRight(), "?");
        com.ib.client.Util.appendNonEmptyString(sb, "multiplier", contract.multiplier());
        com.ib.client.Util.appendNonEmptyString(sb, "exchange", contract.exchange());
        com.ib.client.Util.appendNonEmptyString(sb, "primaryExch", contract.primaryExch());
        com.ib.client.Util.appendNonEmptyString(sb, "currency", contract.currency());
        com.ib.client.Util.appendNonEmptyString(sb, "localSymbol", contract.localSymbol());
        com.ib.client.Util.appendNonEmptyString(sb, "tradingClass", contract.tradingClass());
        com.ib.client.Util.appendNonEmptyString(sb, "type", order.getOrderType());
        com.ib.client.Util.appendValidDoubleValue(sb, "lmtPrice", order.lmtPrice());
        com.ib.client.Util.appendValidDoubleValue(sb, "auxPrice", order.auxPrice());
        com.ib.client.Util.appendNonEmptyString(sb, "TIF", order.getTif());
        com.ib.client.Util.appendNonEmptyString(sb, "openClose", order.openClose());
        com.ib.client.Util.appendValidIntValue(sb, "origin", order.origin());
        com.ib.client.Util.appendNonEmptyString(sb, "orderRef", order.orderRef());
        com.ib.client.Util.appendValidIntValue(sb, "clientId", order.clientId());
        com.ib.client.Util.appendValidIntValue(sb, "parentId", order.parentId());
        com.ib.client.Util.appendValidIntValue(sb, "permId", order.permId());
        com.ib.client.Util.appendBooleanFlag(sb, "outsideRth", order.outsideRth());
        com.ib.client.Util.appendBooleanFlag(sb, "hidden", order.hidden());
        com.ib.client.Util.appendValidDoubleValue(sb, "discretionaryAmt", order.discretionaryAmt());
        com.ib.client.Util.appendPositiveIntValue(sb, "displaySize", order.displaySize());
        com.ib.client.Util.appendValidIntValue(sb, "triggerMethod", order.getTriggerMethod());
        com.ib.client.Util.appendNonEmptyString(sb, "goodAfterTime", order.goodAfterTime());
        com.ib.client.Util.appendNonEmptyString(sb, "goodTillDate", order.goodTillDate());
        com.ib.client.Util.appendNonEmptyString(sb, "faGroup", order.faGroup());
        com.ib.client.Util.appendNonEmptyString(sb, "faMethod", order.getFaMethod());
        com.ib.client.Util.appendNonEmptyString(sb, "faPercentage", order.faPercentage());
        com.ib.client.Util.appendPositiveIntValue(sb, "shortSaleSlot", order.shortSaleSlot());
        if (order.shortSaleSlot() > 0) {
            com.ib.client.Util.appendNonEmptyString(sb, "designatedLocation", order.designatedLocation());
            com.ib.client.Util.appendValidIntValue(sb, "exemptCode", order.exemptCode());
        }
        com.ib.client.Util.appendNonEmptyString(sb, "ocaGroup", order.ocaGroup());
        com.ib.client.Util.appendPositiveIntValue(sb, "ocaType", order.getOcaType());
        com.ib.client.Util.appendNonEmptyString(sb, "rule80A", order.getRule80A());
        com.ib.client.Util.appendBooleanFlag(sb, "blockOrder", order.blockOrder());
        com.ib.client.Util.appendBooleanFlag(sb, "sweepToFill", order.sweepToFill());
        com.ib.client.Util.appendBooleanFlag(sb, "allOrNone", order.allOrNone());
        com.ib.client.Util.appendValidIntValue(sb, "minQty", order.minQty());
        com.ib.client.Util.appendValidDoubleValue(sb, "percentOffset", order.percentOffset());
        com.ib.client.Util.appendBooleanFlag(sb, "optOutSmartRouting", order.optOutSmartRouting());
        com.ib.client.Util.appendValidDoubleValue(sb, "startingPrice", order.startingPrice());
        com.ib.client.Util.appendValidDoubleValue(sb, "stockRefPrice", order.stockRefPrice());
        com.ib.client.Util.appendValidDoubleValue(sb, "delta", order.delta());
        com.ib.client.Util.appendValidDoubleValue(sb, "stockRangeLower", order.stockRangeLower());
        com.ib.client.Util.appendValidDoubleValue(sb, "stockRangeUpper", order.stockRangeUpper());
        
        com.ib.client.Util.appendValidDoubleValue(sb, "volatility", order.volatility());
        if(order.volatility() != Double.MAX_VALUE) {
            com.ib.client.Util.appendPositiveIntValue(sb, "volatilityType", order.getVolatilityType());
            com.ib.client.Util.appendNonEmptyString(sb, "deltaNeutralOrderType", order.getDeltaNeutralOrderType());
            com.ib.client.Util.appendValidDoubleValue(sb, "deltaNeutralAuxPrice", order.deltaNeutralAuxPrice());
            com.ib.client.Util.appendPositiveIntValue(sb, "deltaNeutralConId", order.deltaNeutralConId());
            com.ib.client.Util.appendNonEmptyString(sb, "deltaNeutralSettlingFirm", order.deltaNeutralSettlingFirm());
            com.ib.client.Util.appendNonEmptyString(sb, "deltaNeutralClearingAccount", order.deltaNeutralClearingAccount());
            com.ib.client.Util.appendNonEmptyString(sb, "deltaNeutralClearingIntent", order.deltaNeutralClearingIntent());
            com.ib.client.Util.appendNonEmptyString(sb, "deltaNeutralOpenClose", order.deltaNeutralOpenClose(), "?");
            com.ib.client.Util.appendBooleanFlag(sb, "deltaNeutralShortSale", order.deltaNeutralShortSale());
            if (order.deltaNeutralShortSale()) {
                com.ib.client.Util.appendValidIntValue(sb, "deltaNeutralShortSaleSlot", order.deltaNeutralShortSaleSlot());
                com.ib.client.Util.appendNonEmptyString(sb, "deltaNeutralDesignatedLocation", order.deltaNeutralDesignatedLocation());
            }
            com.ib.client.Util.appendBooleanFlag(sb, "continuousUpdate", order.continuousUpdate());
            com.ib.client.Util.appendValidIntValue(sb, "referencePriceType", order.getReferencePriceType());
        }
        com.ib.client.Util.appendValidDoubleValue(sb, "trailStopPrice", order.trailStopPrice());
        com.ib.client.Util.appendValidDoubleValue(sb, "trailingPercent", order.trailingPercent());
        com.ib.client.Util.appendValidDoubleValue(sb, "lmtPriceOffset", order.lmtPriceOffset());
        com.ib.client.Util.appendValidIntValue(sb, "scaleInitLevelSize", order.scaleInitLevelSize());
        com.ib.client.Util.appendValidIntValue(sb, "scaleSubsLevelSize", order.scaleSubsLevelSize());
        com.ib.client.Util.appendValidDoubleValue(sb, "scalePriceIncrement", order.scalePriceIncrement());
        com.ib.client.Util.appendValidDoubleValue(sb, "scalePriceAdjustValue", order.scalePriceAdjustValue());
        com.ib.client.Util.appendValidIntValue(sb, "scalePriceAdjustInterval", order.scalePriceAdjustInterval());
        com.ib.client.Util.appendValidDoubleValue(sb, "scaleProfitOffset", order.scaleProfitOffset());
        com.ib.client.Util.appendBooleanFlag(sb, "scaleAutoReset", order.scaleAutoReset());
        com.ib.client.Util.appendValidIntValue(sb, "scaleInitPosition", order.scaleInitPosition());
        com.ib.client.Util.appendValidIntValue(sb, "scaleInitFillQty", order.scaleInitFillQty());
        com.ib.client.Util.appendBooleanFlag(sb, "scaleRandomPercent", order.scaleRandomPercent());
        com.ib.client.Util.appendNonEmptyString(sb, "hedgeType", order.getHedgeType());
        com.ib.client.Util.appendNonEmptyString(sb, "hedgeParam", order.hedgeParam());
        com.ib.client.Util.appendNonEmptyString(sb, "account", order.account());
        com.ib.client.Util.appendNonEmptyString(sb, "modelCode", order.modelCode());
        com.ib.client.Util.appendNonEmptyString(sb, "settlingFirm", order.settlingFirm());
        com.ib.client.Util.appendNonEmptyString(sb, "clearingAccount", order.clearingAccount());
        com.ib.client.Util.appendNonEmptyString(sb, "clearingIntent", order.clearingIntent());
        com.ib.client.Util.appendBooleanFlag(sb, "notHeld", order.notHeld());
        com.ib.client.Util.appendBooleanFlag(sb, "whatIf", order.whatIf());
        com.ib.client.Util.appendBooleanFlag(sb, "solicited", order.solicited());
        com.ib.client.Util.appendBooleanFlag(sb, "randomizeSize", order.randomizeSize());
        com.ib.client.Util.appendBooleanFlag(sb, "randomizePrice", order.randomizePrice());
        com.ib.client.Util.appendBooleanFlag(sb, "dontUseAutoPriceForHedge", order.dontUseAutoPriceForHedge());
        com.ib.client.Util.appendBooleanFlag(sb, "isOmsContainer", order.isOmsContainer());
        com.ib.client.Util.appendBooleanFlag(sb, "discretionaryUpToLimitPrice", order.discretionaryUpToLimitPrice());
        com.ib.client.Util.appendBooleanFlag(sb, "usePriceMgmtAlgo", order.usePriceMgmtAlgo());

        if (com.ib.client.Util.IsPegBenchOrder(order.orderType())) {
            com.ib.client.Util.appendPositiveIntValue(sb, "referenceContractId", order.referenceContractId());
            com.ib.client.Util.appendBooleanFlag(sb, "isPeggedChangeAmountDecrease", order.isPeggedChangeAmountDecrease());
            com.ib.client.Util.appendValidDoubleValue(sb, "peggedChangeAmount", order.peggedChangeAmount());
            com.ib.client.Util.appendValidDoubleValue(sb, "referenceChangeAmount", order.referenceChangeAmount());
            com.ib.client.Util.appendNonEmptyString(sb, "referenceExchangeId", order.referenceExchangeId());
        }
        
        if ("BAG".equals(contract.getSecType())) {
            if (contract.comboLegsDescrip() != null) {
                com.ib.client.Util.appendNonEmptyString(sb, "comboLegsDescrip", contract.comboLegsDescrip());
            }
            
            sb.append(" comboLegs={");
            if (contract.comboLegs() != null) {
                for (int i = 0; i < contract.comboLegs().size(); ++i) {
                    ComboLeg comboLeg = contract.comboLegs().get(i);
                    sb.append(" leg ").append(i + 1).append(":");
                    com.ib.client.Util.appendPositiveIntValue(sb, "conid", comboLeg.conid());
                    com.ib.client.Util.appendPositiveIntValue(sb, "ratio", comboLeg.ratio());
                    com.ib.client.Util.appendNonEmptyString(sb, "action", comboLeg.getAction());
                    com.ib.client.Util.appendNonEmptyString(sb, "exchange", comboLeg.exchange());
                    com.ib.client.Util.appendValidIntValue(sb, "openClose", comboLeg.getOpenClose());
                    com.ib.client.Util.appendBooleanFlag(sb, "shortSaleSlot", comboLeg.shortSaleSlot());
                    if (comboLeg.shortSaleSlot() > 0) {
                        com.ib.client.Util.appendNonEmptyString(sb, "designatedLocation", comboLeg.designatedLocation());
                        com.ib.client.Util.appendValidIntValue(sb, "exemptCode", comboLeg.exemptCode());
                    }
                    
                    if (order.orderComboLegs() != null && contract.comboLegs().size() == order.orderComboLegs().size()) {
                        OrderComboLeg orderComboLeg = order.orderComboLegs().get(i);
                        com.ib.client.Util.appendValidDoubleValue(sb, "price", orderComboLeg.price());
                    }
                    sb.append(';');
                }
            }
            sb.append('}');
            
            if (order.basisPoints() != Double.MAX_VALUE) {
                com.ib.client.Util.appendValidDoubleValue(sb, "basisPoints", order.basisPoints());
                com.ib.client.Util.appendValidIntValue(sb, "basisPointsType", order.basisPointsType());
            }
        }
        
        if (contract.deltaNeutralContract() != null) {
            DeltaNeutralContract deltaNeutralContract = contract.deltaNeutralContract();
            sb.append(" deltaNeutralContract={");
            com.ib.client.Util.appendPositiveIntValue(sb, "conid", deltaNeutralContract.conid());
            com.ib.client.Util.appendValidDoubleValue(sb, "delta", deltaNeutralContract.delta());
            com.ib.client.Util.appendValidDoubleValue(sb, "price", deltaNeutralContract.price());
            sb.append("}");
        }

        if (!com.ib.client.Util.StringIsEmpty(order.getAlgoStrategy())) {
            com.ib.client.Util.appendNonEmptyString(sb, "algoStrategy", order.getAlgoStrategy());
            if (order.algoParams() != null && order.algoParams().size() > 0) {
                sb.append(" algoParams={");
                for (com.ib.client.TagValue param : order.algoParams()) {
                    sb.append(param.m_tag).append('=').append(param.m_value).append(',');
                }
                if (!order.algoParams().isEmpty()) {
                    sb.setLength(sb.length() - 1);
                }
                sb.append('}');
            }
        }
        
        if ("BAG".equals(contract.getSecType())) {
            if (order.smartComboRoutingParams() != null && order.smartComboRoutingParams().size() > 0) {
                sb.append(" smartComboRoutingParams={");
                for (TagValue param : order.smartComboRoutingParams()) {
                    sb.append(param.m_tag).append('=').append(param.m_value).append(',');
                }
                if (!order.smartComboRoutingParams().isEmpty()) {
                    sb.setLength(sb.length() - 1);
                }
                sb.append('}');
            }
        }

        
        com.ib.client.Util.appendNonEmptyString(sb, "autoCancelDate", order.autoCancelDate());
        com.ib.client.Util.appendNonEmptyString(sb, "filledQuantity", order.filledQuantity().toString());
        com.ib.client.Util.appendPositiveIntValue(sb, "refFuturesConId", order.refFuturesConId());
        com.ib.client.Util.appendBooleanFlag(sb, "autoCancelParent", order.autoCancelParent());
        com.ib.client.Util.appendNonEmptyString(sb, "shareholder", order.shareholder());
        com.ib.client.Util.appendBooleanFlag(sb, "imbalanceOnly", order.imbalanceOnly());
        com.ib.client.Util.appendBooleanFlag(sb, "routeMarketableToBbo", order.routeMarketableToBbo());
        com.ib.client.Util.appendValidLongValue(sb, "parentPermId", order.parentPermId());
        com.ib.client.Util.appendValidIntValue(sb, "duration", order.duration());
        com.ib.client.Util.appendValidIntValue(sb, "postToAts", order.postToAts());

        com.ib.client.Util.appendValidIntValue(sb, "minTradeQty", order.minTradeQty());
        com.ib.client.Util.appendValidIntValue(sb, "minCompeteSize", order.minCompeteSize());
        if (order.competeAgainstBestOffset() != Double.MAX_VALUE) {
            if (order.isCompeteAgainstBestOffsetUpToMid()) {
                sb.append(com.ib.client.Util.SPACE_SYMBOL).append("competeAgainstBestOffsetUpToMid");
            } else {
                sb.append(com.ib.client.Util.SPACE_SYMBOL).append("competeAgainstBestOffset").append(com.ib.client.Util.EQUALS_SIGN).append(order.competeAgainstBestOffset());
            }
        }
        com.ib.client.Util.appendValidDoubleValue(sb, "midOffsetAtWhole", order.midOffsetAtWhole());
        com.ib.client.Util.appendValidDoubleValue(sb, "midOffsetAtHalf", order.midOffsetAtHalf());
        com.ib.client.Util.appendNonEmptyString(sb, "customerAccount", order.customerAccount());
        com.ib.client.Util.appendBooleanFlag(sb, "professionalCustomer", order.professionalCustomer());
        com.ib.client.Util.appendNonEmptyString(sb, "bondAccruedInterest", order.bondAccruedInterest());
        
        com.ib.client.Util.appendNonEmptyString(sb, "status", orderState.getStatus());
        com.ib.client.Util.appendNonEmptyString(sb, "completedTime", orderState.completedTime());
        com.ib.client.Util.appendNonEmptyString(sb, "completedStatus", orderState.completedStatus());
        
        if (order.whatIf()) {
            com.ib.client.Util.appendValidDoubleValue(sb, "initMarginBefore", orderState.initMarginBefore());
            com.ib.client.Util.appendValidDoubleValue(sb, "maintMarginBefore", orderState.maintMarginBefore());
            com.ib.client.Util.appendValidDoubleValue(sb, "equityWithLoanBefore", orderState.equityWithLoanBefore());
            com.ib.client.Util.appendValidDoubleValue(sb, "initMarginChange", orderState.initMarginChange());
            com.ib.client.Util.appendValidDoubleValue(sb, "maintMarginChange", orderState.maintMarginChange());
            com.ib.client.Util.appendValidDoubleValue(sb, "equityWithLoanChange", orderState.equityWithLoanChange());
            com.ib.client.Util.appendValidDoubleValue(sb, "initMarginAfter", orderState.initMarginAfter());
            com.ib.client.Util.appendValidDoubleValue(sb, "maintMarginAfter", orderState.maintMarginAfter());
            com.ib.client.Util.appendValidDoubleValue(sb, "equityWithLoanAfter", orderState.equityWithLoanAfter());
            com.ib.client.Util.appendValidDoubleValue(sb, "commission", orderState.commission());
            com.ib.client.Util.appendValidDoubleValue(sb, "minCommission", orderState.minCommission());
            com.ib.client.Util.appendValidDoubleValue(sb, "maxCommission", orderState.maxCommission());
            com.ib.client.Util.appendNonEmptyString(sb, "commissionCurrency", orderState.commissionCurrency());
            Util.appendNonEmptyString(sb, "warningText", orderState.warningText());
        }
        
        if (order.conditions() != null && order.conditions().size() > 0) {
            sb.append(" conditions={");
            for (OrderCondition condition : order.conditions()) {
                sb.append(condition).append(";");
            }
            if (!order.conditions().isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            sb.append('}');
        }
    }
    
    
}