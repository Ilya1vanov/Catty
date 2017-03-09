package com.ilya.ivanov.catty_catalog.controller;

/**
 * Created by ivano on 07.03.2017.
 */
public interface StageController {
    /**
     * This method will be invoked by StageDriver, when stage was
     * changed to the current
     */
    default void onStageShown() {}
}
