package com.ilya.ivanov.catty_catalog.view;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.view.stages.StageDriver;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class View extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
        StageDriver.setStage("login");
	}

	public static void main(String[] args) {
		launch(args);
	}
}