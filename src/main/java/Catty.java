import javafx.application.Application;
import javafx.stage.Stage;
import model.quotatimer.QuotaTimer;
import org.apache.commons.configuration2.Configuration;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.concurrent.*;

import controller.DataController;
import properties.PropertiesLoader;
import view.View;

public class Catty extends Application {
    private static final Logger log = Logger.getLogger(Catty.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
        QuotaTimer.getInstance().start();
//        Files.createDirectory(Paths.get("temp"));

        try {
            Executors.newSingleThreadExecutor().submit(() -> {
                DataController.dao.connect("src/main/resources/db");
                return false;
            });
        } catch (Exception e) {
            // terminate
            throw e;
        }

        View.hideAllAndShow("login");
	}

    @Override
    public void stop() throws Exception {
        DataController.dao.disconnect();
    }

	public static void main(String[] args) {
		launch(args);
	}
}