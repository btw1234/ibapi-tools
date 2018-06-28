package jcontractinspector;

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
public class Depth {
    StringProperty maker = new SimpleStringProperty("");
    IntegerProperty size = new SimpleIntegerProperty(0);
    DoubleProperty price = new SimpleDoubleProperty(0.0);
    DoubleProperty last = new SimpleDoubleProperty(0.0);
    IntegerProperty cumSz = new SimpleIntegerProperty(0);
    
    static int reqId;//for all 

}
