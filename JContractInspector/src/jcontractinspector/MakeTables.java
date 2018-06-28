package jcontractinspector;

import com.ib.client.Contract;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author brian
 */
public class MakeTables {

    
    
    private MakeTables() {}
    
    static void contracts(TableView tblContracts){
        
        TableColumn<Contract, Number> conid = new TableColumn<>("conid");
        TableColumn<Contract, String> secType = new TableColumn<>("sec\nType");
        TableColumn<Contract, String> symbol = new TableColumn<>("symbol");
        TableColumn<Contract, String> localSymbol = new TableColumn<>("local\nSymbol");
        TableColumn<Contract, String> tradingClass = new TableColumn<>("trading\nClass");
        TableColumn<Contract, String> expiry = new TableColumn<>("expiry");
        TableColumn<Contract, String> exchange = new TableColumn<>("exchange");
        TableColumn<Contract, String> primaryExch = new TableColumn<>("primary\nExch");
        TableColumn<Contract, String> currency = new TableColumn<>("currency");
        TableColumn<Contract, Number> strike = new TableColumn<>("strike");
        TableColumn<Contract, String> right = new TableColumn<>("right");
        TableColumn<Contract, String> multiplier = new TableColumn<>("multiplier");
//        TableColumn<Contract, String> secIdType = new TableColumn<>("secIdType");
//        TableColumn<Contract, String> secId = new TableColumn<>("secId");
//        
        conid.setCellValueFactory((param) -> new ReadOnlyIntegerWrapper(param.getValue().conid()));
        secType.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().secType().getApiString()));
        symbol.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().symbol()));
        localSymbol.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().localSymbol()));
        tradingClass.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().tradingClass()));
        expiry.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().lastTradeDateOrContractMonth()));
        exchange.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().exchange()));
        primaryExch.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().primaryExch()));
        currency.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().currency()));
        strike.setCellValueFactory((param) -> new ReadOnlyDoubleWrapper(param.getValue().strike()));
        right.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().right().getApiString()));
        multiplier.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().multiplier()));
//        secIdType.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().secIdType().getApiString()));
//        secId.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().secId()));
//        
        tblContracts.getColumns().setAll(conid,localSymbol,secType,symbol,tradingClass,exchange,currency,expiry,strike,right,multiplier);
        tblContracts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }

    static void tickers(TableView<Ticker> tblTickers) {
        TableColumn<Ticker, String> symbol = new TableColumn<>("symbol");
        TableColumn<Ticker, Number> bidSz = new TableColumn<>("bidSz");
        TableColumn<Ticker, Number> bid = new TableColumn<>("bid");
        TableColumn<Ticker, Number> last = new TableColumn<>("last");
        TableColumn<Ticker, Number> lastSz = new TableColumn<>("lastSz");
        TableColumn<Ticker, Number> ask = new TableColumn<>("ask");
        TableColumn<Ticker, Number> askSz = new TableColumn<>("askSz");
        TableColumn<Ticker, Number> volume = new TableColumn<>("volume");
        
        symbol.setCellValueFactory((param) -> param.getValue().symbol);
        bidSz.setCellValueFactory((param) -> param.getValue().bidSz);
        bid.setCellValueFactory((param) -> param.getValue().bid);
        last.setCellValueFactory((param) -> param.getValue().last);
        lastSz.setCellValueFactory((param) -> param.getValue().lastSz);
        ask.setCellValueFactory((param) -> param.getValue().ask);
        askSz.setCellValueFactory((param) -> param.getValue().askSz);
        volume.setCellValueFactory((param) -> param.getValue().volume);
        
        tblTickers.getColumns().setAll(symbol, bidSz, bid, last, lastSz, ask, askSz, volume);
        tblTickers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }

    static void depth(TableView<Depth> tblDepthBid, TableView<Depth> tblDepthAsk) {
        TableColumn<Depth, String> makerBid = new TableColumn<>("maker");
        TableColumn<Depth, String> makerAsk = new TableColumn<>("maker");
        TableColumn<Depth, Number> bidSz = new TableColumn<>("bidSz");
        TableColumn<Depth, Number> askSz = new TableColumn<>("askSz");
        TableColumn<Depth, Number> bid = new TableColumn<>("bid");
        TableColumn<Depth, Number> ask = new TableColumn<>("ask");
        TableColumn<Depth, Number> cumSzBid = new TableColumn<>("cumSz");
        TableColumn<Depth, Number> cumSzAsk = new TableColumn<>("cumSz");

        makerBid.setCellValueFactory((param) -> param.getValue().maker);
        makerAsk.setCellValueFactory((param) -> param.getValue().maker);
        bidSz.setCellValueFactory((param) -> param.getValue().size);
        askSz.setCellValueFactory((param) -> param.getValue().size);
        bid.setCellValueFactory((param) -> param.getValue().price);
        ask.setCellValueFactory((param) -> param.getValue().price);
        cumSzBid.setCellValueFactory((param) -> param.getValue().cumSz);
        cumSzAsk.setCellValueFactory((param) -> param.getValue().cumSz);

        tblDepthBid.getColumns().setAll(makerBid, bid, bidSz, cumSzBid);
        tblDepthAsk.getColumns().setAll(makerAsk, ask, askSz, cumSzAsk);
    }

}
