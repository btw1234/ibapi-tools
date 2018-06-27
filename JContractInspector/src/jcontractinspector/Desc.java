package jcontractinspector;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * ** @author brian
 */
public class Desc {
    static final String strConId = "int 	ConId - The unique IB contract identifier. ";
    static final String strSymbol = "string  - The underlying's asset symbol. ";
    static final String strSecType = "string  - The security's type: STK - stock (or ETF) OPT - option FUT - future IND - index FOP - futures option CASH - forex pair BAG - combo WAR - warrant BOND- bond CMDTY- commodity NEWS- news FUND- mutual fund. ";
    static final String strLastTradeDateOrContractMonth = "string  - The contract's last trading day or contract month (for Options and Futures). static final Strings with format YYYYMM will be interpreted as the Contract Month whereas YYYYMMDD will be interpreted as Last Trading Day. ";
    static final String strStrike = "double  - The option's strike price. ";
    static final String strRight = "string  - Either Put or Call (i.e. Options). Valid values are P, PUT, C, CALL. ";
    static final String strMultiplier = "string  - The instrument's multiplier (i.e. options, futures). ";
    static final String strExchange = "string  - The destination exchange. ";
    static final String strCurrency = "string  - The underlying's currency. ";
    static final String strLocalSymbol = "string  - The contract's symbol within its primary exchange For options, this will be the OCC symbol. ";
    static final String strPrimaryExch = "string  - The contract's primary exchange. For smart routed contracts, used to define contract in case of ambiguity. Should be defined as native exchange of contract, e.g. ISLAND for MSFT For exchanges which contain a period in name, will only be part of exchange name prior to period, i.e. ENEXT for ENEXT.BE. ";
    static final String strTradingClass = "string  - The trading class name for this contract. Available in TWS contract description window as well. For example, GBL Dec '13 future's trading class is FGBL.";
    static final String strIncludeExpired = "bool  - If set to true, contract details requests and historical data queries can be performed pertaining to expired futures contracts. Expired options or other instrument types are not available. ";
    static final String strSecIdType = "string  - Security's identifier when querying contract's details or placing orders ISIN - Example: Apple: US0378331005 CUSIP - Example: Apple: 037833100. ";
    static final String strSecId = "string  - Identifier of the security type. More...";
    
    static Tooltip makeTT(String s){
        Tooltip tt = new Tooltip(s);
        tt.setPrefWidth(300);
        tt.setHideDelay(Duration.millis(s.length() * 10));
        tt.setWrapText(true);
        tt.getStyleClass().add("tooltip");
        return tt;
    }
}
