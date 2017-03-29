package view;

import controller.Controller;
import controller.stagecontrollers.StageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.plist.ParseException;
import properties.PropertiesLoader;
import properties.PropertiesSetter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Statically stages, set their properties and drive their mapping.
 * Created by Илья on 05.03.2017.
 */
public class View {
    /* currently shown stage */
    private static String currentStage;

    /* mapping stageName -> Stage */
    private static final HashMap<String, Stage> STAGES = new HashMap<>();

    /* rows per page, shown if the search results */
    public static final int ROWS_PER_PAGE = 30;

    /* names of the stages, storing in View */
    private static final ArrayList stageNames = new ArrayList();

    /* Initialize STAGES and their properties */
    static {
        try {
            LinkedHashMap<String, Configuration> map = PropertiesLoader.loadXMLConfiguration(new FileInputStream("src/main/resources/xml/STAGES.xml"), "name", "stage");

            for (Map.Entry<String, Configuration> entry : map.entrySet()) {
                String stageName = entry.getKey();
                Stage stage = new Stage();

                try {
                    PropertiesSetter.setProperties(stage, Stage.class, entry.getValue());
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                // loadProperties scene from view/fxml/'stageName'_stage.fxml
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(View.class.getResource("../fxml/" + stageName + "_stage.fxml"));
                stage.setScene(new Scene(loader.load()));

                // loadProperties and store controller
                Controller.addController(stageName, loader.getController());

                // set catty-icon to all STAGES
                stage.getIcons().add(new Image(View.class.getResourceAsStream("../pics/catty-icon.png")));

                STAGES.put(stageName, stage);
                stageNames.add(stageName);
            }
        } catch (ParseException | ConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getStageNames() {return (String[]) stageNames.toArray(); }

    /**
     * Returns stage according the given name.
     * @param stageName name of the stage
     * @return the stage to which the specified name is mapped, or null if this view contains no mapping for the given name
     */
    public static Stage getStage(String stageName) {
        return STAGES.get(stageName);
    }

    /**
     * Returns currently shown stage.
     * @return Currently shown stage.
     */
    public static Stage getCurrentStage() {
        return STAGES.get(currentStage);
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
    public static void hideAllAndShow(String stageName) throws RuntimeException {
        if (currentStage != null)
            STAGES.get(currentStage).hide();
        Stage newStage = STAGES.get(stageName);
        if (newStage == null)
            throw new RuntimeException("No such stage: " + stageName);
        newStage.show();
        currentStage = stageName;

        // invoke onStageShown if Controller implements StageController interface
        if (Controller.getController(stageName) instanceof StageController)
            ((StageController) Controller.getController(stageName)).onStageShown();
    }
}