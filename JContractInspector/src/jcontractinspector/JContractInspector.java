package jcontractinspector;

import java.net.URL;
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
        
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
//        config.setProperty("iconified", String.format("%b", stage.isIconified()));
//        stage.setIconified(false);
//        config.setProperty("screenX", stage.getX());
//        config.setProperty("screenY", String.format("%.1f", stage.getY()));
//        config.setProperty("screenW", String.format("%.1f", stage.getWidth()));
//        config.setProperty("screenH", String.format("%.1f", stage.getHeight()));
//        try (OutputStream out = new FileOutputStream(new File("JContract.properties"))) {
//            config.store(out, "JContract properties");
//        } catch (IOException ex) {}
//        
        ctlr.disconnect();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
