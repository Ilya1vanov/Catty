package com.ilya.ivanov.catty_cathalog.controller;


import java.io.File;
import java.util.List;

import com.ilya.ivanov.catty_cathalog.model._abstract.AbstractFileObject;
import com.ilya.ivanov.catty_cathalog.model._enum.User;
import com.ilya.ivanov.catty_cathalog.view.java.View;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class MainSceneController {
	@FXML HBox controlButtons;
	@FXML Button retrieveButton;
	@FXML Button deleteButton;
	@FXML Button addButton;
	
	@FXML TextField searchField;
		
	@FXML StackPane tableStack;
	
	@FXML VBox workingTreeLayout;
	@FXML TreeTableView<AbstractFileObject> workingTreeL__Table;
	@FXML TreeTableColumn<AbstractFileObject, String> workingTreeT__fileColumn;
	@FXML TreeTableColumn<AbstractFileObject, String> workingTreeT__sizeColumn;
	
	@FXML VBox searchResultsLayout;
	@FXML TableView<AbstractFileObject> searchL__resultTable;
	@FXML TableColumn<AbstractFileObject, String> searchT__fileColumn;
	@FXML TableColumn<AbstractFileObject, String> searchT__sizeColumn;
	@FXML Pagination searchL__paginator;

	@FXML Text userNameLetter;
	@FXML Label workingTreeL__statusBar;	
	
	public static User user;

	
	@FXML public void initialize() {
//		workingTreeL__Table;
		// search
		searchField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {
				
			}
		});
		
		userNameLetter.setText(String.valueOf(user.getName().charAt(0)).toUpperCase());
		
		switch (user) {
		case GUEST:
			workingTreeL__statusBar.setText("Read-only mode");
			controlButtons.setDisable(true);
			break;
		case USER:
			deleteButton.setDisable(true);
			break;
		default:
			break;
		}
		
		workingTreeT__fileColumn.setCellValueFactory(
	            (TreeTableColumn.CellDataFeatures<AbstractFileObject, String> param) -> 
	            new ReadOnlyStringWrapper(param.getValue().getValue().getName())
	        );

		workingTreeT__sizeColumn.setCellValueFactory(
	            (TreeTableColumn.CellDataFeatures<AbstractFileObject, String> param) -> 
	            new ReadOnlyStringWrapper(param.getValue().getValue().getSize())
	        );
		
		// pullWorkingTree
		
		
		
		workingTreeL__Table.setRoot(null);
	}
	
	@FXML public void handleSignOut(MouseEvent event) {
		try {
			Parent loginRoot = FXMLLoader.load(getClass().getResource("../view/fxml/login_scene.fxml"));
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			
			primaryStage.setTitle("Welcome");
			primaryStage.setWidth(View.LOGIN_WIDTH);
			primaryStage.setHeight(View.LOGIN_HEIGHT);
			primaryStage.setResizable(false);
	
			primaryStage.setScene(new Scene(loginRoot));
	
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML public void handleAddButtonAction(ActionEvent event) {
		// add some file types
		// parse selected partition
		
		Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select files");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("Document Files", "*.txt", "*.pdf", ".doc", ".docx", ".rtf"),
		        new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
		        new ExtensionFilter("Video Files", "*.avi", "*.mp4", "*.wav"),
		        new ExtensionFilter("All Files", "*.*"));
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
		if (selectedFiles != null) {
			// pushRequest
			System.out.println("Files selected!");
			for (File file : selectedFiles) {
				System.out.println(file.getName());
				// 
			}
		}
	}

	@FXML public void handleDeleteButtonAction(ActionEvent event) {
		// deleteRequest
		// render new view
	}

	@FXML public void handleSearch(ActionEvent event) {
		if (!searchField.getText().isEmpty()) {
			workingTreeLayout.setVisible(false);
			searchResultsLayout.setVisible(true);
		}
	}

}
