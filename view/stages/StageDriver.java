package com.ilya.ivanov.catty_catalog.view.stages;

import com.ilya.ivanov.catty_catalog.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Илья on 05.03.2017.
 */
public class StageDriver {
    static Stage currentStage;

    public static Stage getCurrentStage() {
        return currentStage;
    }

    private static final HashMap<String, HashMap<String, Object>> stageProperties = new HashMap<>();

    static {
        HashMap<String, Object> loginProperties = new HashMap<>();
        loginProperties.put("Title", "Welcome");
        loginProperties.put("MinWidth", 350.0);
        loginProperties.put("MinHeight", 225.0);
        loginProperties.put("Resizable", false);

        HashMap<String, Object> mainProperties = new HashMap<>();
        mainProperties.put("Title", "Catty");
        mainProperties.put("MinWidth", 1024.0);
        mainProperties.put("MinHeight", 768.0);
        mainProperties.put("Resizable", true);

        stageProperties.put("login", loginProperties);
        stageProperties.put("main", mainProperties);
    }

    private static final HashMap<String, Stage> stages = new HashMap<>();

    static {
        for (Map.Entry<String, HashMap<String, Object>> stageEntry : stageProperties.entrySet()) {
            Stage stage = new Stage();

            for (Map.Entry<String, Object> property : stageEntry.getValue().entrySet())
                try {
                    Arrays.stream(Stage.class.getDeclaredMethods())
                            .filter(method -> method.getName().equals("set" + property.getKey()))
                            .findFirst()
                            .get()
                            .invoke(stage, property.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            try {
                stage.setScene(new Scene(FXMLLoader.load(View.class.getResource("./fxml/" + stageEntry.getKey() + "_scene.fxml"))));
                stages.put(stageEntry.getKey(), stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStage(String stageName) {
        if (currentStage != null)
            currentStage.hide();
        currentStage = stages.get(stageName);
        currentStage.show();
    }
}
