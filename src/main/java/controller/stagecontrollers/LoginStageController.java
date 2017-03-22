package controller.stagecontrollers;


import controller.DataController;
import javafx.scene.control.TextField;
import model.Model;
import model.user.Guest;
import view.View;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;
import java.util.Set;

public class LoginStageController implements StageController {
	@FXML private javafx.scene.text.Text warningText;
	@FXML private javafx.scene.control.PasswordField passwordField;
	@FXML private TextField loginField;

	@FXML Button submitButton;

    @Size(min = 3, message = "Too short login!")
	private StringProperty login;

    @Size(min = 3, message = "Too short pass!")
    private StringProperty pass;

    private static final Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

	@FXML public void handleSubmitButtonAction() {
        Set<ConstraintViolation<LoginStageController>> violations = validator.validate(this);
        violations.forEach(System.out::println);

        Model.setUser(DataController.dao.signIn(loginField.getText(), passwordField.getText()));
		if (Model.getUser() != null) {
            View.hideAllAndShow("main");
		}
		else {
            warningText.setVisible(true);
            passwordField.requestFocus();
        }
    }

	@FXML public void handleSignInAsGuest() {
        Model.setUser(new Guest());
        View.hideAllAndShow("main");
	}

	@Override
	public void onStageShown() {
	    loginField.setText("");
        passwordField.setText("");

        loginField.requestFocus();
    }
	
	@FXML public void initialize() {
	    submitButton.setDisable(false);
        login = loginField.textProperty();
        pass = passwordField.textProperty();

	    // disable submit button if one of the fields is empty
//		submitButton.disableProperty().bind(Bindings.or(loginField.textProperty().isEmpty(), passwordField.textProperty().isEmpty()));
	}

	@FXML public void handleLoginChanged() {
		warningText.setVisible(false);
	}

	@FXML public void handlePasswordChanged() {
		warningText.setVisible(false);
	}
}
