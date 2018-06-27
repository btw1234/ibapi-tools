package jcontractinspector;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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
    @FXML private Tab tabContracts;
    @FXML private TableView<Contract> tblContracts;
    @FXML private Button btnReqMktData;
    @FXML private Button btnReqSnapshot;
    @FXML private Button btnReqDepth;
    @FXML private Label lblContracts;
    @FXML private Tab tabTickers;
    @FXML private Tab tabDepth;
    @FXML private TextArea logArea;
    
    private EReaderSignal signal;
    private EClientSocket  client;
    private EReader reader;
    private Wrapper wrapper;
        
    @FXML void initialize() {
        cbxSecType.getItems().addAll("STK","FUT","OPT");
        cbxSecType.getSelectionModel().clearAndSelect(0);
        cbxSecIdType.getItems().addAll("CUSIP","ISIN");
        
        wrapper = new Wrapper(logArea);
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
            logArea.appendText("Connected to Tws server version "
                    + client.serverVersion() + " at "
                    + client.TwsConnectionTime() + "\n");
        }

        reader = new EReader(client, signal);

        reader.start();

        new Thread() {
            @Override
            public void run() {
                while (client.isConnected()) {
                    signal.waitForSignal();
                    try {
                        reader.processMsgs();
                    } catch (IOException e) {
                        wrapper.error(e);
                    }
                }
            }
        }.start();
    }

    private void disconnect() {
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

    }

    @FXML void reqDepth(ActionEvent event) {

    }

    @FXML void reqMktData(ActionEvent event) {

    }

    @FXML void reqSnapshot(ActionEvent event) {

    }

}
