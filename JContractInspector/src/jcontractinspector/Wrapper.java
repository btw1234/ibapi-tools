package jcontractinspector;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDescription;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.DepthMktDataDescription;
import com.ib.client.EWrapperMsgGenerator;
import com.ib.client.Execution;
import com.ib.client.FamilyCode;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.SoftDollarTier;
import com.ib.client.TickAttr;
import java.time.LocalTime;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextArea;

/**
 *
 * @author brian
 */
public class Wrapper implements com.ib.client.EWrapper{
    
    public final BooleanProperty isConnected = new SimpleBooleanProperty(false);
    private final FormCtlr ctlr;
    
    Wrapper(FormCtlr ctlr) {
        this.ctlr = ctlr;
    }

    @Override
    public void nextValidId(int i) {
        Platform.runLater(() -> {
            ctlr.log("connected ok");
            ctlr.log(EWrapperMsgGenerator.nextValidId(i));
            isConnected.set(true);
        });
    }

    @Override
    public void contractDetails(int id, ContractDetails cd) {
        Platform.runLater(() -> {
                ctlr.addContractToTable(id, cd);
        });
    }

    @Override
    public void contractDetailsEnd(int i) {
        Platform.runLater(() -> {
            ctlr.log(EWrapperMsgGenerator.contractDetailsEnd(i));
            ctlr.contractDetailsEnd();//to update number rec'd
        });
    }

    @Override
    public void tickPrice(int id, int field, double price, TickAttr ta) {
        Platform.runLater(() -> {
                ctlr.reqs.get(id).tickPrice(field, price);
        });
    }

    @Override
    public void tickSize(int id, int field, int size) {
        Platform.runLater(() -> {
                ctlr.reqs.get(id).tickSize(field, size);
        });
    }

    @Override
    public void updateMktDepth(int id, int pos, int op, int side, double price, int vol) {
        Platform.runLater(() -> {
            ctlr.updateMktDepth(id, pos, op, side, price, vol);
        });
    }

    @Override
    public void tickOptionComputation(int i, int i1, double d, double d1, double d2, double d3, double d4, double d5, double d6, double d7) {}

    @Override
    public void tickGeneric(int i, int i1, double d) {}

    @Override
    public void tickString(int i, int i1, String string) {}

    @Override
    public void tickEFP(int i, int i1, double d, String string, double d1, int i2, String string1, double d2, double d3) {}

    @Override
    public void orderStatus(int i, String string, double d, double d1, double d2, int i1, int i2, double d3, int i3, String string1) {}

    @Override
    public void openOrder(int i, Contract cntrct, Order order, OrderState os) {}

    @Override
    public void openOrderEnd() {}

    @Override
    public void updateAccountValue(String string, String string1, String string2, String string3) {}

    @Override
    public void updatePortfolio(Contract cntrct, double d, double d1, double d2, double d3, double d4, double d5, String string) {}

    @Override
    public void updateAccountTime(String string) {}

    @Override
    public void accountDownloadEnd(String string) {}

    @Override
    public void bondContractDetails(int i, ContractDetails cd) {}

    @Override
    public void execDetails(int i, Contract cntrct, Execution exctn) {}

    @Override
    public void execDetailsEnd(int i) {}

    @Override
    public void updateMktDepthL2(int i, int i1, String string, int i2, int i3, double d, int i4) {}

    @Override
    public void updateNewsBulletin(int i, int i1, String string, String string1) {}

    @Override
    public void managedAccounts(String string) {}

    @Override
    public void receiveFA(int i, String string) {}

    @Override
    public void historicalData(int i, String string, double d, double d1, double d2, double d3, int i1, int i2, double d4, boolean bln) {}

    @Override
    public void scannerParameters(String string) {}

    @Override
    public void scannerData(int i, int i1, ContractDetails cd, String string, String string1, String string2, String string3) {}

    @Override
    public void scannerDataEnd(int i) {}

    @Override
    public void realtimeBar(int i, long l, double d, double d1, double d2, double d3, long l1, double d4, int i1) {}

    @Override
    public void currentTime(long l) {}

    @Override
    public void fundamentalData(int i, String string) {}

    @Override
    public void deltaNeutralValidation(int i, DeltaNeutralContract dnc) {}

    @Override
    public void tickSnapshotEnd(int i) {}

    @Override
    public void marketDataType(int i, int i1) {}

    @Override
    public void commissionReport(CommissionReport cr) {}

    @Override
    public void position(String string, Contract cntrct, double d, double d1) {}

    @Override
    public void positionEnd() {}

    @Override
    public void accountSummary(int i, String string, String string1, String string2, String string3) {}

    @Override
    public void accountSummaryEnd(int i) {}

    @Override
    public void verifyMessageAPI(String string) {}

    @Override
    public void verifyCompleted(boolean bln, String string) {}

    @Override
    public void verifyAndAuthMessageAPI(String string, String string1) {}

    @Override
    public void verifyAndAuthCompleted(boolean bln, String string) {}

    @Override
    public void displayGroupList(int i, String string) {}

    @Override
    public void displayGroupUpdated(int i, String string) {}

    @Override
    public void error(Exception excptn) {
        ctlr.log(EWrapperMsgGenerator.error(excptn));
    }

    @Override
    public void error(String string) {
        ctlr.log(EWrapperMsgGenerator.error(string));
    }

    @Override
    public void error(int i, int i1, String string) {
        ctlr.log(EWrapperMsgGenerator.error(i, i1, string));
        
    }

    @Override
    public void connectionClosed() {
        ctlr.log("connection closed");
        Platform.runLater(() -> 
                isConnected.set(false)
        );
    }

    @Override
    public void connectAck() {}

    @Override
    public void positionMulti(int i, String string, String string1, Contract cntrct, double d, double d1) {}

    @Override
    public void positionMultiEnd(int i) {}

    @Override
    public void accountUpdateMulti(int i, String string, String string1, String string2, String string3, String string4) {}

    @Override
    public void accountUpdateMultiEnd(int i) {}

    @Override
    public void securityDefinitionOptionalParameter(int i, String string, int i1, String string1, String string2, Set<String> set, Set<Double> set1) {}

    @Override
    public void securityDefinitionOptionalParameterEnd(int i) {}

    @Override
    public void softDollarTiers(int i, SoftDollarTier[] sdts) {}

    @Override
    public void familyCodes(FamilyCode[] fcs) {}

    @Override
    public void symbolSamples(int i, ContractDescription[] cds) {}

    @Override
    public void historicalDataEnd(int i, String string, String string1) {}

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] dmdds) {}

    @Override
    public void tickNews(int i, long l, String string, String string1, String string2, String string3) {}

}
