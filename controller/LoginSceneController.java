package com.ilya.ivanov.catty_cathalog.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginSceneController {
	@FXML
	private javafx.scene.text.Text warningText;
	@FXML
	private javafx.scene.control.PasswordField passwordField;
	@FXML
	private javafx.scene.control.TextField loginField;

	@FXML
	public void handleSubmitButtonAction(ActionEvent event) throws IOException {
		warningText.setVisible(true);

		Parent mainRoot = FXMLLoader.load(getClass().getResource("../view/fxml/main_scene.fxml"));
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		primaryStage.setMinWidth(600);
		primaryStage.setMinHeight(400);
		primaryStage.setResizable(true);

		primaryStage.setScene(new Scene(mainRoot, 600, 400));
		primaryStage.show();
	}
}
