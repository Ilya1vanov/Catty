package com.ilya.ivanov.catty_catalog.controller.stagecontrollers;

import com.ilya.ivanov.catty_catalog.controller.DataController;
import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.user.Guest;
import com.ilya.ivanov.catty_catalog.view.View;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;

public class LoginStageController implements StageController {
	@FXML private javafx.scene.text.Text warningText;
	@FXML private javafx.scene.control.PasswordField passwordField;

    @Size(min = 3, message = "Too short login!")
	@FXML private javafx.scene.control.TextField loginField;

	@FXML Button submitButton;

    @Size(min = 3, message = "Too short login!")
	private StringProperty log;

    private StringProperty pass;

    private static Validator validator;

	@FXML public void handleSubmitButtonAction() {
//        validator.validate(this);
//        validator.validateProperty(loginField, "text");

        Model.setUser(DataController.dao.signIn(loginField.getText(), passwordField.getText()));
		if (Model.getUser() != null) {
            View.setStage("main");
		}
		else {
            warningText.setVisible(true);
            passwordField.requestFocus();
        }
    }

	@FXML public void handleSignInAsGuest() {
        Model.setUser(new Guest());
        View.setStage("main");
	}

	@Override
	public void onStageShown() {
	    loginField.setText("");
        passwordField.setText("");

        loginField.requestFocus();
    }
	
	@FXML public void initialize() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//
//	    log.bind(loginField.textProperty());
//        pass = passwordField.textProperty();

	    // disable submit button if one of the fields is empty
		submitButton.disableProperty().bind(Bindings.or(loginField.textProperty().isEmpty(), passwordField.textProperty().isEmpty()));
	}

	@FXML public void handleLoginChanged() {
		warningText.setVisible(false);
	}

	@FXML public void handlePasswordChanged() {
		warningText.setVisible(false);
	}
}
