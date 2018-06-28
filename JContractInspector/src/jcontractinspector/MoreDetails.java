package jcontractinspector;

import com.ib.client.ContractDetails;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author brian
 */
class MoreDetails extends Stage{
        /*
        Contract 	Contract
 	A fully-defined Contract object. 
 
string 	MarketName
 	The market name for this product. 
 
double 	MinTick
 	The minimum allowed price variation. Note that many securities vary their minimum tick size according to their price. This value will only show the smallest of the different minimum tick sizes regardless of the product's price. Full information about the minimum increment price structure can be obtained with the reqMarketRule function or the IB Contract and Security Search site. 
 
int 	PriceMagnifier
 	Allows execution and strike prices to be reported consistently with market data, historical data and the order price, i.e. Z on LIFFE is reported in Index points and not GBP. In TWS versions prior to 972, the price magnifier is used in defining future option strike prices (e.g. in the API the strike is specified in dollars, but in TWS it is specified in cents). In TWS versions 972 and higher, the price magnifier is not used in defining futures option strike prices so they are consistent in TWS and the API. 
 
string 	OrderTypes
 	Supported order types for this product. 
 
string 	ValidExchanges
 	Valid exchange fields when placing an order for this contract.
The list of exchanges will is provided in the same order as the corresponding MarketRuleIds list. 
 
int 	UnderConId
 	For derivatives, the contract ID (conID) of the underlying instrument. 
 
string 	LongName
 	Descriptive name of the product. 
 
string 	ContractMonth
 	Typically the contract month of the underlying for a Future contract. 
 
string 	Industry
 	The industry classification of the underlying/product. For example, Financial. 
 
string 	Category
 	The industry category of the underlying. For example, InvestmentSvc. 
 
string 	Subcategory
 	The industry subcategory of the underlying. For example, Brokerage. 
 
string 	TimeZoneId
 	The time zone for the trading hours of the product. For example, EST. 
 
string 	TradingHours
 	The trading hours of the product. This value will contain the trading hours of the current day as well as the next's. For example, 20090507:0700-1830,1830-2330;20090508:CLOSED. In TWS versions 965+ there is an option in the Global Configuration API settings to return 1 month of trading hours. In TWS version 970+, the format includes the date of the closing time to clarify potential ambiguity, ex: 20180323:0400-20180323:2000;20180326:0400-20180326:2000 The trading hours will correspond to the hours for the product on the associated exchange. The same instrument can have different hours on different exchanges. 
 
string 	LiquidHours
 	The liquid hours of the product. This value will contain the liquid hours (regular trading hours) of the contract on the specified exchange. Format for TWS versions until 969: 20090507:0700-1830,1830-2330;20090508:CLOSED. In TWS versions 965+ there is an option in the Global Configuration API settings to return 1 month of trading hours. In TWS v970 and above, the format includes the date of the closing time to clarify potential ambiguity, e.g. 20180323:0930-20180323:1600;20180326:0930-20180326:1600. 
 
string 	EvRule
 	Contains the Economic Value Rule name and the respective optional argument. The two values should be separated by a colon. For example, aussieBond:YearsToExpiration=3. When the optional argument is not present, the first value will be followed by a colon. 
 
double 	EvMultiplier
 	Tells you approximately how much the market value of a contract would change if the price were to change by 1. It cannot be used to get market value by multiplying the price by the approximate multiplier. 
 
int 	MdSizeMultiplier
 	MD Size Multiplier. Returns the size multiplier for values returned to tickSize from a market data request. Generally 100 for US stocks and 1 for other instruments. 
 
int 	AggGroup
 	Aggregated group Indicates the smart-routing group to which a contract belongs. contracts which cannot be smart-routed have aggGroup = -1. 
 
List< TagValue > 	SecIdList
 	A list of contract identifiers that the customer is allowed to view. CUSIP/ISIN/etc. For US stocks, receiving the ISIN requires the CUSIP market data subscription. For Bonds, the CUSIP or ISIN is input directly into the symbol field of the Contract class. 
 
string 	UnderSymbol
 	For derivatives, the symbol of the underlying contract. 
 
string 	UnderSecType
 	For derivatives, returns the underlying security type. 
 
string 	MarketRuleIds
 	The list of market rule IDs separated by comma Market rule IDs can be used to determine the minimum price increment at a given price. 
 
string 	RealExpirationDate
 	Real expiration date. Requires TWS 968+ and API v973.04+. Python API specifically requires API v973.06+. 
 
string 	LastTradeTime
 	Last trade time. 
 
string 	Cusip
 	The nine-character bond CUSIP. For Bonds only. Receiving CUSIPs requires a CUSIP market data subscription. 
 
string 	Ratings
 	Identifies the credit rating of the issuer. This field is not currently available from the TWS API. For Bonds only. A higher credit rating generally indicates a less risky investment. Bond ratings are from Moody's and S&P respectively. Not currently implemented due to bond market data restrictions. 
 
string 	DescAppend
 	A description string containing further descriptive information about the bond. For Bonds only. 
 
string 	BondType
 	The type of bond, such as "CORP.". 
 
string 	CouponType
 	The type of bond coupon. This field is currently not available from the TWS API. For Bonds only. 
 
bool 	Callable
 	If true, the bond can be called by the issuer under certain conditions. This field is currently not available from the TWS API. For Bonds only. 
 
bool 	Putable
 	Values are True or False. If true, the bond can be sold back to the issuer under certain conditions. This field is currently not available from the TWS API. For Bonds only. 
 
double 	Coupon
 	The interest rate used to calculate the amount you will receive in interest payments over the course of the year. This field is currently not available from the TWS API. For Bonds only. 
 
bool 	Convertible
 	Values are True or False. If true, the bond can be converted to stock under certain conditions. This field is currently not available from the TWS API. For Bonds only. 
 
string 	Maturity
 	he date on which the issuer must repay the face value of the bond. This field is currently not available from the TWS API. For Bonds only. Not currently implemented due to bond market data restrictions. 
 
string 	IssueDate
 	The date the bond was issued. This field is currently not available from the TWS API. For Bonds only. Not currently implemented due to bond market data restrictions. 
 
string 	NextOptionDate
 	Only if bond has embedded options. This field is currently not available from the TWS API. Refers to callable bonds and puttable bonds. Available in TWS description window for bonds. 
 
string 	NextOptionType
 	Type of embedded option. This field is currently not available from the TWS API. Only if bond has embedded options. 
 
bool 	NextOptionPartial
 	Only if bond has embedded options. This field is currently not available from the TWS API. For Bonds only. 
 
string 	Notes
 	If populated for the bond in IB's database. For Bonds only. 
 
        */

    MoreDetails(ContractDetails cd) {
        String more = cd.toString();
        String[] ss = more.split("\n");
        String less = "";
        
        for (String s : ss) {
            if (s.length() < 100){
                less += s + "\n";
            }
                
        }
        Label l = new Label(less);
        
        l.setMaxWidth(600);
        l.setWrapText(true);

        Scene s = new Scene(l);
        setScene(s);
    }
    
}
