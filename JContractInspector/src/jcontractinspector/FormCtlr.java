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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
    @FXML private TableView<Contract> tblContracts;
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
    }
    
    void contractDetailsEnd(){
        lblContracts.setText(tblContracts.getItems().size() + " items");
    }

    @FXML void reqMktData(ActionEvent event) {
        if (((Button)event.getSource()).getId().contains("elayed")){
            client.reqMarketDataType(MarketDataType.DELAYED);
        } else {
            client.reqMarketDataType(MarketDataType.REALTIME);
        }
        tblContracts.getSelectionModel().getSelectedItems().forEach(cont -> {
            Ticker ticker = new Ticker(reqId, cont, false);
            reqs.put(reqId, ticker);
            tblTickers.getItems().add(ticker);
            client.reqMktData(reqId++, cont, "", false, null);
            log("reqMktData for " + cont.localSymbol());
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
        Contract cont = tblContracts.getSelectionModel().getSelectedItem();
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
            Ticker ticker = new Ticker(reqId, cont, true);
            reqs.put(reqId, ticker);
            tblTickers.getItems().add(ticker);
            client.reqMktData(reqId++, cont, "", true, null);
            log("snapshot for " + cont.localSymbol());
        });
    }
    
    void addContractToTable(int i, ContractDetails cd){
        tblContracts.getItems().add(cd.contract());
    }

}
