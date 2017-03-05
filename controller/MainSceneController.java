package com.ilya.ivanov.catty_catalog.controller;


import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.view.chooser.FileChooserPopup;
import com.ilya.ivanov.catty_catalog.view.stages.StageDriver;
import com.ilya.ivanov.catty_catalog.view.columns.ColumnModel;
import com.ilya.ivanov.catty_catalog.view.columns.TableColumnFactory;
import com.ilya.ivanov.catty_catalog.view.columns.TreeTableColumnFactory;
import com.ilya.ivanov.catty_catalog.view.tabs.TabFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.List;
import java.util.Map;


public class MainSceneController {
	@FXML HBox controlButtons;
    @FXML Button addButton;
	@FXML Button deleteButton;
	
	@FXML TextField searchField;
	@FXML TabPane tabPane;

//	@FXML StackPane tableStack;
	
	@FXML VBox workingTreeLayout;
	@FXML TreeTableView<AbstractFileObject> workingTreeL__TreeTable;

	@FXML VBox searchResultsLayout;
	@FXML TableView<AbstractFileObject> searchL__resultTable;
	@FXML Pagination searchL__pagination;

	@FXML Text userNameLetter;
	@FXML Label workingTreeL__statusBar;	


	@FXML public void initialize() {
		// search
		// ESC handler
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE))
                handleSearchCrossClicked();
        });

        // if search field is empty then show working tree layout
        searchField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(0)) {
                workingTreeLayout.setVisible(true);
                searchResultsLayout.setVisible(false);
            }
        });

        // i should place code below in snippet than executes when user was chosen or when login scene changes to main

        // show first capitalized letter of username in top-right corner
//		userNameLetter.setText(String.valueOf(Model.user.getName().charAt(0)).toUpperCase());

        // set workspace according to user's permissions
//		switch (Model.user.getType()) {
//			case guest:
//				workingTreeL__statusBar.setText("Read-only mode");
//				controlButtons.setDisable(true);
//				break;
//			case user:
//				workingTreeL__statusBar.setText("Your quota for uploading files is " + Model.user.getQuota() + " for today");
//				break;
//			case admin:
//				workingTreeL__statusBar.setText("Full-capacity mode");
//				break;
//		}


        // create tabs and attach them to tabPane according to the list of file categories
        for (String category : Model.fileCategories) {
            tabPane.getTabs().add(TabFactory.getInstance(category));
        }

        // deselect tabs. important cause otherwise selected tab
        // will be empty. selection will be fired manually below
        tabPane.getSelectionModel().clearSelection();

        // set new root of working tree when tab selection was changed
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (workingTreeL__TreeTable.isVisible())
                workingTreeL__TreeTable.setRoot(Model.getRoot(newValue.getId()));
        });

        // manual fired selection
        tabPane.getSelectionModel().selectFirst();

        // set columns resize policy
        searchL__resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // creating TableColumns
        for (Map.Entry<String, ColumnModel> entry : Model.modelColumnsMap.entrySet())
            searchL__resultTable
                    .getColumns()
                    .add((TableColumn<AbstractFileObject, String>) (new TableColumnFactory()).getColumn(entry.getKey(), entry.getValue()));

        // set columns resize policy
        workingTreeL__TreeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        // creating TreeTableColumns
        for (Map.Entry<String, ColumnModel> entry : Model.modelColumnsMap.entrySet())
            workingTreeL__TreeTable
                    .getColumns()
                    .add((TreeTableColumn) (new TreeTableColumnFactory()).getColumn(entry.getKey(), entry.getValue()));
	}
	
	@FXML public void handleSignOut() {
		try {
            StageDriver.setStage("login");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML public void handleAddButtonAction(ActionEvent event) {
		List<File> selectedFiles = FileChooserPopup.callFileChooser(tabPane.getSelectionModel().getSelectedItem().getId());
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
		tabPane.getSelectionModel().getSelectedItem().getId();
		// deleteRequest
		// render new view
	}

	@FXML public void handleSearch(ActionEvent event) {
		if (!searchField.getText().isEmpty()) {
			workingTreeLayout.setVisible(false);
			searchResultsLayout.setVisible(true);
		}
	}

	public void handleSearchCrossClicked() {
		searchField.setText("");
	}
}
