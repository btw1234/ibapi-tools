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
    
    static void Contracts(TableView tv){
        
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
        tv.getColumns().setAll(conid,localSymbol,secType,symbol,tradingClass,exchange,currency,expiry,strike,right,multiplier);
        tv.setEditable(false);
        tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }

}
