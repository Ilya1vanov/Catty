package com.ilya.ivanov.catty_cathalog.controller;


import java.sql.SQLException;

//import java.sql.Date;

import com.ilya.ivanov.catty_cathalog.model._enum.UserType;
import com.ilya.ivanov.catty_cathalog.model.implementation.DAODatabase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginSceneController {
	@FXML
	private javafx.scene.text.Text warningText;
	@FXML
	private javafx.scene.control.PasswordField passwordField;
	@FXML
	private javafx.scene.control.TextField loginField;
	@FXML Button submitButton;
	
	public static DAODatabase dao = new DAODatabase();

	@FXML
	public void handleSubmitButtonAction(ActionEvent event) {
		// Date date = Date.valueOf("");
		if (dao.signIn(loginField.getText(), passwordField.getText())) {
			
		}
		else {
			warningText.setVisible(true);
			passwordField.requestFocus();
		}
	}

	@FXML public void handleSignInAsGuest(MouseEvent event) {
		changeSceneToMain((Stage) ((Node) event.getSource()).getScene().getWindow());
	}
	
	@FXML
	public void initialize() {
		ChangeListener<Number> listner = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {
				submitButton.setDisable(
						loginField.getText().isEmpty() ||
						passwordField.getText().isEmpty());
			}
		};
		loginField.lengthProperty().addListener(listner);
		passwordField.lengthProperty().addListener(listner);
		
		try {
			dao.connect();
		} catch (SQLException e) {
			// error window
			// terminate or try again
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void handleLoginChanged() {
		warningText.setVisible(false);
	}

	@FXML public void handlePasswordChenged() {
		warningText.setVisible(false);
	}
	
	private void changeSceneToMain(Stage primaryStage) {
		try {
			Parent mainRoot = FXMLLoader.load(getClass().getResource("../view/fxml/main_scene.fxml"));
			primaryStage.setTitle("Catty");

			primaryStage.setMinWidth(600);
			primaryStage.setMinHeight(400);
			primaryStage.setResizable(true);

			primaryStage.setScene(new Scene(mainRoot));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML public void handlePasswordFieldAction(ActionEvent event) {
		submitButton.requestFocus();
		handleSubmitButtonAction(event);
	}
}
