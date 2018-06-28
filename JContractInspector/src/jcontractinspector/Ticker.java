package jcontractinspector;

import com.ib.client.Contract;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brian
 */
public class Ticker {
    StringProperty symbol = new SimpleStringProperty("");
    IntegerProperty bidSz = new SimpleIntegerProperty(0);
    DoubleProperty bid = new SimpleDoubleProperty(0.0);
    DoubleProperty last = new SimpleDoubleProperty(0.0);
    IntegerProperty lastSz = new SimpleIntegerProperty(0);
    DoubleProperty ask = new SimpleDoubleProperty(0.0);
    IntegerProperty askSz = new SimpleIntegerProperty(0);
    IntegerProperty volume = new SimpleIntegerProperty(0);
    /**So I can check for up/dn/flat color*/
    DoubleProperty prevClose = new SimpleDoubleProperty(0.0);
    StringProperty style = new SimpleStringProperty("flat");
    
    final int reqId;
    final boolean isSnapshot;
    
          
    Ticker(int reqId, Contract cont, boolean isSnapshot) {
        this.reqId = reqId;
        symbol.set(cont.localSymbol());
        this.isSnapshot = isSnapshot;
    }
    
    void tickPrice(int field, double price){
        switch (field) {
            case  1 : bid.set(price); return;
            case  2 : ask.set(price); return;
            case  4 : last.set(price); break;
            case  9 : prevClose.set(price); break;
        }
        
        if (last.get() > prevClose.get()) style.set("up");
        else if (last.get() < prevClose.get()) style.set("down");
        else if (last.get() == prevClose.get()) style.set("flat");
    }
    
    void tickSize(int field, int size){
        switch (field) {
            case  0 : bidSz.set(size); break;
            case  3 : askSz.set(size); break;
            case  5 : lastSz.set(size); break;
            case  8 : volume.set(size); break;
        }
        
    }
}
