package controller.stagecontrollers;

/**
 * Super class for FXML controllers.
 * Created by ivanov on 07.03.2017.
 */
public interface StageController {
    /**
     * This method will be invoked by View, when stage was
     * changed to the current
     */
    default void onStageShown() {}
}
