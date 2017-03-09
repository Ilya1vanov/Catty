package com.ilya.ivanov.catty_catalog.controller;


import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.user.Guest;
import com.ilya.ivanov.catty_catalog.view.stages.StageDriver;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginStageController implements StageController {
	@FXML private javafx.scene.text.Text warningText;
	@FXML private javafx.scene.control.PasswordField passwordField;
	@FXML private javafx.scene.control.TextField loginField;
	@FXML Button submitButton;

	@FXML public void handleSubmitButtonAction() {
		// Validator
        Model.setUser(DataController.dao.signIn(loginField.getText(), passwordField.getText()));
		if (Model.getUser() != null) {
            StageDriver.setStage("main");
		}
		else {
			warningText.setVisible(true);
			passwordField.requestFocus();
		}
	}

	@FXML public void handleSignInAsGuest() {
        Model.setUser(new Guest());
        StageDriver.setStage("main");
	}

	@Override
	public void onStageShown() {
	    loginField.setText("");
        passwordField.setText("");

        loginField.requestFocus();
    }
	
	@FXML public void initialize() {
	    // disable submit button if one of the fields is empty
		ChangeListener<Number> listener = (arg0, oldVal, newVal) -> submitButton.setDisable(
                loginField.getText().isEmpty() ||
                passwordField.getText().isEmpty());
        // add this listener to both buttons
		loginField.lengthProperty().addListener(listener);
		passwordField.lengthProperty().addListener(listener);
	}

	@FXML public void handleLoginChanged() {
		warningText.setVisible(false);
	}

	@FXML public void handlePasswordChanged() {
		warningText.setVisible(false);
	}
}
