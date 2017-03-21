package com.ilya.ivanov.catty_catalog.controller;

import com.ilya.ivanov.catty_catalog.view.View;

import java.util.HashMap;

/**
 * Created by Илья on 12.03.2017.
 */
public class Controller {
    private static final HashMap<String, Object> controllers = new HashMap<>();

    /**
     * Add controller with specified name to the storage.
     * Stage with given name can be received with
     * View.getStage(name).
     * @param name Name of the stage.
     * @param controller Controller of the stage.
     */
    public static void addController(String name, Object controller) {
        controllers.put(name, controller);
    }

    /**
     * Returns controller of stage with specified name.
     * @param stageName Name of the stage.
     * @return Controller of stage with specified name.
     */
    public static Object getController(String stageName) {
//        if (View.getCurrentStageName() == null)
//            return null;
        return controllers.get(stageName);
    }

    /**
     * Returns controller of currently shown stage.
     * Returns null if no stage is shown.
     * @return controller of the currently shown stage; null
     * if nothing is shown
     */
    public static Object getCurrentController() {
        return controllers.get(View.getCurrentStageName());
    }
}
