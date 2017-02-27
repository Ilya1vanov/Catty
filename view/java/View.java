package com.ilya.ivanov.catty_cathalog.view.java;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {
	public static double LOGIN_WIDTH = 350;
	public static double LOGIN_HEIGHT = 225;
	
	public static double MAIN_WIDTH = 600;
	public static double MAIN_HEIGHT = 400;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			primaryStage.setTitle("Welcome");
			primaryStage.setMinWidth(LOGIN_WIDTH);
			primaryStage.setMinHeight(LOGIN_HEIGHT);
			primaryStage.setResizable(false);
	
			Parent loginRoot = FXMLLoader.load(getClass().getResource("../fxml/login_scene.fxml"));
			primaryStage.setScene(new Scene(loginRoot));
	
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
