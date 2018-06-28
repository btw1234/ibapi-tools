package jcontractinspector;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.MarketDataType;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FormCtlr {

    @FXML private HBox topHBox;
    @FXML private Button btnConnect;
    @FXML private TextField txtInetAddr;
    @FXML private TextField txtPort;
    @FXML private TextField txtClientID;
    @FXML private Label lblStatus;
    @FXML private TextField txtContractId;    
    @FXML private ChoiceBox<String> cbxSecType;    
    @FXML private TextField txtSymbol;
    @FXML private TextField txtLocalSymbol;
    @FXML private TextField txtTradingClass;
    @FXML private TextField txtExpiry;
    @FXML private TextField txtExchange;
    @FXML private TextField txtPrimaryExchange;
    @FXML private TextField txtCurrency;
    @FXML private TextField txtStrike;
    @FXML private TextField txtRight;
    @FXML private TextField txtMultiplier;
    @FXML private ChoiceBox<String> cbxSecIdType;
    @FXML private TextField txtSecId;
    @FXML private CheckBox chkIncludeExpired;
    @FXML private Button btnReqContractDetails;
    @FXML private TabPane tabPane;
    @FXML private Tab tabContracts;
    @FXML private TableView<ContractDetails> tblContracts;
    @FXML private Button btnReqMktData;
    @FXML private Button btnReqSnapshot;
    @FXML private Button btnReqDepth;
    @FXML private Label lblContracts;
    @FXML private Tab tabTickers;
    @FXML private Tab tabDepth;
    @FXML private TextArea logArea;
    @FXML private TableView<Ticker> tblTickers;
    @FXML private Button btnCancelData;
    @FXML private TableView<Depth> tblDepthBid;
    @FXML private TableView<Depth> tblDepthAsk;
    @FXML private Button btnCancelDepth;
    
    private EReaderSignal signal;
    private EClientSocket  client;
    private EReader reader;
    private Wrapper wrapper;
    
    private int reqId = 1;
    final Map<Integer, Ticker> reqs = new HashMap<>();

    void log(String s){
        Platform.runLater(() ->
            logArea.appendText(LocalTime.now() + " " + s + "\n")
        );
    }
    
    @FXML void initialize() {
        cbxSecType.getItems().addAll("STK","FUT","OPT","IND","FOP","CASH","BAG","WAR","BOND","CMDTY","NEWS","FUND");
        cbxSecType.getSelectionModel().clearAndSelect(0);
        cbxSecIdType.getItems().addAll("CUSIP","ISIN");
        
        txtContractId.setTooltip(Desc.makeTT(Desc.strConId));
        cbxSecType.setTooltip(Desc.makeTT(Desc.strSecType));
        txtSymbol.setTooltip(Desc.makeTT(Desc.strSymbol));
        txtLocalSymbol.setTooltip(Desc.makeTT(Desc.strLocalSymbol));
        txtTradingClass.setTooltip(Desc.makeTT(Desc.strTradingClass));
        txtExpiry.setTooltip(Desc.makeTT(Desc.strLastTradeDateOrContractMonth));
        txtExchange.setTooltip(Desc.makeTT(Desc.strExchange));
        txtPrimaryExchange.setTooltip(Desc.makeTT(Desc.strPrimaryExch));
        txtCurrency.setTooltip(Desc.makeTT(Desc.strCurrency));
        txtStrike.setTooltip(Desc.makeTT(Desc.strStrike));
        txtRight.setTooltip(Desc.makeTT(Desc.strRight));
        txtMultiplier.setTooltip(Desc.makeTT(Desc.strMultiplier));
        cbxSecIdType.setTooltip(Desc.makeTT(Desc.strSecIdType));
        txtSecId.setTooltip(Desc.makeTT(Desc.strSecId));
        chkIncludeExpired.setTooltip(Desc.makeTT(Desc.strIncludeExpired));

        MakeTables.contracts(tblContracts);
        MakeTables.tickers(tblTickers);
        MakeTables.depth(tblDepthBid, tblDepthAsk);
        
        wrapper = new Wrapper(this);
        signal = new EJavaSignal();
        client = new EClientSocket(wrapper, signal);
        
        lblStatus.textProperty().bind(Bindings.createStringBinding(
                () -> wrapper.isConnected.get() ? "Connected" : "Disconnected",
                wrapper.isConnected));
        
        btnConnect.textProperty().bind(Bindings.createStringBinding(
                () -> wrapper.isConnected.get() ? "Disconnect" : "Connect",
                wrapper.isConnected));
        
        wrapper.isConnected.addListener((obs,ov,nv) -> {
            if (nv) {
                topHBox.getStyleClass().add("connected");
                lblStatus.getStyleClass().add("connected");
            } else {
                topHBox.getStyleClass().remove("connected");
                lblStatus.getStyleClass().remove("connected");
            }
            
        });
    }
    
    private void connect() {
        if (client.isConnected()) {
            return;
        }

        client.eConnect(
                txtInetAddr.getText(),
                Integer.parseInt(txtPort.getText()),
                Integer.parseInt(txtClientID.getText()));

        if (client.isConnected()) {
            log("Connected to TWS server version "
                    + client.serverVersion() + " at " + client.TwsConnectionTime());
        }

        reader = new EReader(client, signal);

        reader.start();

        new Thread(() -> {
            while (client.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (IOException e) {
                    wrapper.error(e);
                }
            }
        }).start();
        
    }

    void disconnect() {
        wrapper.isConnected.set(false);
        client.eDisconnect();
    }

    @FXML void connect(ActionEvent event) {
        if (btnConnect.getText().equals("Connect")){
            connect();
        } else { 
            disconnect();
        }
    }
    
    @FXML void reqContractDetails(ActionEvent event) {
        Contract c = new Contract();
        try {
            c.conid(Integer.parseInt(txtContractId.getText()));
        } catch (NumberFormatException nfe) {/*ok I guess*/}
        c.secType(cbxSecType.getValue());
        c.symbol(txtSymbol.getText());
        c.localSymbol(txtLocalSymbol.getText());
        c.tradingClass(txtTradingClass.getText());
        c.lastTradeDateOrContractMonth(txtExpiry.getText());
        c.exchange(txtExchange.getText());
        c.primaryExch(txtPrimaryExchange.getText());
        c.currency(txtCurrency.getText());
        try {
            c.strike(Double.parseDouble(txtStrike.getText()));
        } catch (NumberFormatException nfe) {/*ok I guess*/}    
        c.right(txtRight.getText());
        c.multiplier(txtMultiplier.getText());
        c.secIdType(cbxSecIdType.getValue());
        c.secId(txtSecId.getText());
        c.includeExpired(chkIncludeExpired.isSelected());
        
        tblContracts.getItems().clear();//so a new request doesn't fail with old data
        client.reqContractDetails(0, c);
        tabPane.getSelectionModel().select(tabContracts);
    }
    
    void contractDetailsEnd(){
        lblContracts.setText(tblContracts.getItems().size() + " items");
        //sort tables depending on secType
        if (cbxSecType.getValue().equalsIgnoreCase("FUT")) {
            FXCollections.sort(tblContracts.getItems(), (c1, c2)
                    -> c1.contract().lastTradeDateOrContractMonth()
                            .compareTo(c2.contract().lastTradeDateOrContractMonth())
            );
        } else if (cbxSecType.getValue().equalsIgnoreCase("STK")){
            FXCollections.sort(tblContracts.getItems(), (c1, c2)
                    -> c1.contract().exchange().compareTo(c2.contract().exchange())
            );
        }
    }

    @FXML void reqMktData(ActionEvent event) {
        if (((Button)event.getSource()).getId().contains("elayed")){
            client.reqMarketDataType(MarketDataType.DELAYED);
        } else {
            client.reqMarketDataType(MarketDataType.REALTIME);
        }
        tblContracts.getSelectionModel().getSelectedItems().forEach(cont -> {
            Ticker ticker = new Ticker(reqId, cont.contract(), false);
            reqs.put(reqId, ticker);
            tblTickers.getItems().add(ticker);
            client.reqMktData(reqId++, cont.contract(), "", false, null);
            log("reqMktData for " + cont.contract().localSymbol());
        });
        tabPane.getSelectionModel().select(tabTickers);
    }
    
    @FXML void cancelMktData(ActionEvent event) {
        List<Ticker> toRemove = new ArrayList<>();
        tblTickers.getSelectionModel().getSelectedItems().forEach(ticker -> {
            if (!ticker.isSnapshot) client.cancelMktData(ticker.reqId);
            reqs.remove(ticker.reqId);
            log("cancelMktData for " + ticker.symbol.get());
            toRemove.add(ticker);
        });
        tblTickers.getItems().removeAll(toRemove);
    }

    @FXML void reqDepth(ActionEvent event) {
        Contract cont = tblContracts.getSelectionModel().getSelectedItem().contract();
        Depth.reqId = reqId;
        //make 20 depths to re-use
        for (int i = 0; i < 10; i++){
            tblDepthAsk.getItems().add(new Depth());
            tblDepthBid.getItems().add(new Depth());
        }
        client.reqMktDepth(reqId++, cont, 10, null);
        log("reqMktDepth for " + cont.localSymbol());
        tabPane.getSelectionModel().select(tabDepth);
    }

    void updateMktDepth(int id, int pos, int op, int side, double price, int size) {
        if (side == 0){ //ask
            Depth depth = tblDepthAsk.getItems().get(pos);
            depth.price.set(price);
            depth.size.set(size);
            //??cum size every time
        } else { //bid
            Depth depth = tblDepthBid.getItems().get(pos);
            depth.price.set(price);
            depth.size.set(size);
        }
    }

    @FXML void cancelMktDepth(ActionEvent event) {
        client.cancelMktDepth(Depth.reqId);
        log("cancelMktData id" + Depth.reqId);
        tblDepthBid.getItems().clear();
        tblDepthAsk.getItems().clear();
    }
    
    @FXML void reqSnapshot(ActionEvent event) {
        tblContracts.getSelectionModel().getSelectedItems().forEach(cont -> {
            Ticker ticker = new Ticker(reqId, cont.contract(), true);
            reqs.put(reqId, ticker);
            tblTickers.getItems().add(ticker);
            client.reqMktData(reqId++, cont.contract(), "", true, null);
            log("snapshot for " + cont.contract().localSymbol());
        });
    }
    
    void addContractToTable(int i, ContractDetails cd){
        tblContracts.getItems().add(cd);
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
    }

}
