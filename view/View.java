package com.ilya.ivanov.catty_catalog.view;

import com.ilya.ivanov.catty_catalog.controller.Controller;
import com.ilya.ivanov.catty_catalog.controller.stagecontrollers.StageController;
import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.resources.Recource;
import com.ilya.ivanov.catty_catalog.view.fxml.Fxml;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Statically load stages and drive their mapping
 * Created by Илья on 05.03.2017.
 */
public class View {
    private static String currentStage;

    private static final HashMap<String, HashMap<String, Object>> stageProperties = new HashMap<>();

    private static final HashMap<String, Stage> stages = new HashMap<>();

    public static final int rowsPerPage = 30;

    /*
      Declare new stages and their properties here
     */
    static {
        // login stage
        HashMap<String, Object> loginProperties = new HashMap<>();
        loginProperties.put("Title", "Welcome");
        loginProperties.put("MinWidth", 350.0);
        loginProperties.put("MinHeight", 225.0);
        loginProperties.put("Resizable", false);
        stageProperties.put("login", loginProperties);

        // main stage
        HashMap<String, Object> mainProperties = new HashMap<>();
        mainProperties.put("Title", "Catty");
        mainProperties.put("MinWidth", 800.0);
        mainProperties.put("MinHeight", 600.0);
        mainProperties.put("Resizable", true);
        stageProperties.put("main", mainProperties);
    }

    /**
     * Returns currently shown stage.
     * @return Currently shown stage.
     */
    public static final Stage getCurrentStage() {
        return stages.get(currentStage);
    }

    /**
     * Returns name of currently shown stage.
     * @return Name of currently shown stage.
     * Null if no stage is shown.
     */
    public static String getCurrentStageName() {
        return currentStage;
    }

    /**
     * Hides current stage and show scene than represented by
     * stageName parameter.
     * <p>Invoke onStageShown() method of related controller if this controller
     * class implements StageController interface
     * </p>
     * @param stageName name of the stage that stored in
     *                  view/fxml/'name of the stage'_stage.fxml
     * @throws RuntimeException if there are no loaded stage with the given name
     */
    public static void setStage(@NotNull String stageName) throws RuntimeException {
        if (currentStage != null)
            stages.get(currentStage).hide();
        Stage newStage = stages.get(stageName);
        if (newStage == null)
            throw new RuntimeException("No such stage: " + stageName);
        newStage.show();
        currentStage = stageName;

        // invoke onStageShown if Controller implements StegeController interface
        if (Controller.getController(stageName) instanceof StageController)
            ((StageController) Controller.getController(stageName)).onStageShown();
    }

    static {
        for (Map.Entry<String, HashMap<String, Object>> stageEntry : stageProperties.entrySet()) {
            Stage stage = new Stage();

            // set stage properties
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

                // load scene from view/fxml/'stageName'_stage.fxml
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Fxml.class.getResource(stageEntry.getKey() + "_stage.fxml"));
                stage.setScene(new Scene(loader.load()));
                Controller.addController(stageEntry.getKey(), loader.getController());
                stage.getIcons().add(new Image(Recource.class.getResourceAsStream("catty-icon.png")));
                stages.put(stageEntry.getKey(), stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Stage getStage(@NotNull String stageName) {
        return stages.get(stageName);
    }
}