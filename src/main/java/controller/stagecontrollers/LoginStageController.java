package controller.stagecontrollers;

import controller.DataController;
import javafx.scene.control.TextField;
import model.Model;
import model.user.Guest;
import view.View;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Set;

public class LoginStageController implements StageController {
	@FXML private javafx.scene.text.Text warningText;
	@FXML private javafx.scene.control.PasswordField passwordField;
	@FXML private TextField loginField;
	@FXML Button submitButton;

    private static final Validator validator = Model.getValidator();

	@FXML public void handleSubmitButtonAction() {
        Set<ConstraintViolation<Form>> violations = validator.validate(new Form());
        if (!violations.isEmpty()) {
            violations.forEach(formConstraintViolation -> System.out.println(formConstraintViolation.getMessage()));
            violations.forEach(formConstraintViolation -> System.out.println(formConstraintViolation.getPropertyPath()));
            violations.forEach(formConstraintViolation -> System.out.println(formConstraintViolation.getConstraintDescriptor()));
            violations.forEach(formConstraintViolation -> System.out.println(formConstraintViolation.getConstraintDescriptor().getPayload()));
            violations.forEach(formConstraintViolation -> System.out.println(formConstraintViolation.getInvalidValue()));
            violations.forEach(formConstraintViolation -> Arrays.asList(formConstraintViolation.getLeafBean().getClass().getAnnotations()).forEach(System.out::println));
        }

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
	    // disable submit button if one of the fields is empty
		submitButton.disableProperty().bind(Bindings.or(loginField.textProperty().isEmpty(), passwordField.textProperty().isEmpty()));

	}

	@FXML public void handleLoginChanged() {
		warningText.setVisible(false);
	}

	@FXML public void handlePasswordChanged() {
		warningText.setVisible(false);
	}

	private class Form {
        @Size(min = 3, message = "Too short login!")
        @Pattern(regexp = "[a-zA-Z0-9_.-]*", message = "Available symbols: a-z, A-Z, 0-9, '.', '_', '-'.")
	    private String login;
        @Pattern(regexp = "[a-zA-Z0-9_.-]*", message = "Available symbols: a-z, A-Z, 0-9, '.', '_', '-'.")
        @Size(min = 3, message = "Too short pass!")
        private String password;

        Form() {
            this.login = loginField.getText();
            this.password = passwordField.getText();
        }
    }
}
