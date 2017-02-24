package com.ilya.ivanov.catty_cathalog.view.java;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			primaryStage.setTitle("Catty");
			primaryStage.setMinWidth(325);
			primaryStage.setMinHeight(275);
			primaryStage.setResizable(false);
	
			Parent loginRoot = FXMLLoader.load(getClass().getResource("../fxml/login_scene.fxml"));
			primaryStage.setScene(new Scene(loginRoot, 325, 275));
	
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
