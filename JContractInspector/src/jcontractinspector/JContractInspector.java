package jcontractinspector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author brian
 */
public class JContractInspector extends Application {
    private FormCtlr ctlr;
    private final Properties config = new Properties();
    private Stage stage;
    private final String CONFIG = this.getClass().getSimpleName() + ".properties";
    private double x,y; //remember for closing iconified
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Form.fxml").openStream());
        ctlr = fxmlLoader.getController();
        
        Scene scene = new Scene(root);
        URL url = this.getClass().getResource("form.css");
        if (url != null) {
            String css = url.toExternalForm();
            scene.getStylesheets().add(css);
        }
//        scene.getStylesheets().add("jcontractinspector/form.css");//too hard coded

        File propFile = new File(CONFIG);
        propFile.createNewFile();
        config.load(new FileInputStream(propFile));
        stage.setX(Math.max(0, Double.parseDouble(config.getProperty("screenX","0"))));
        stage.setY(Math.max(0, Double.parseDouble(config.getProperty("screenY","0"))));
        stage.setWidth(Double.parseDouble(config.getProperty("screenW","1200")));
        stage.setHeight(Double.parseDouble(config.getProperty("screenH","800")));
        
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
        
        stage.xProperty().addListener((obs,ov,nv) -> {
            if (nv.doubleValue() > 0) x = nv.doubleValue();//compare to desktop
        });
        stage.yProperty().addListener((obs,ov,nv) -> {
            if (nv.doubleValue() > 0) y = nv.doubleValue();//compare to desktop
        });
        
        x = stage.getX();
        y = stage.getY();
        
        ctlr.setData(config);
        
        this.stage = stage;
    }

    @Override public void stop() {
        ctlr.getData(config);
        config.setProperty("screenX", String.format("%.1f", x));
        config.setProperty("screenY", String.format("%.1f", y));
        config.setProperty("screenW", String.format("%.1f", Math.max(500, stage.getWidth())));
        config.setProperty("screenH", String.format("%.1f", Math.max(500, stage.getHeight())));
        try (OutputStream out = new FileOutputStream(new File(CONFIG))) {
            config.store(out, "no comment");
        } catch (IOException ex) {}
        
        ctlr.disconnect();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
