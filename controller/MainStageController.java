package com.ilya.ivanov.catty_catalog.controller;


import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.file.FileObject;
import com.ilya.ivanov.catty_catalog.view.chooser.FileChooserModal;
import com.ilya.ivanov.catty_catalog.view.columns.Columns;
import com.ilya.ivanov.catty_catalog.view.stages.StageDriver;
import com.ilya.ivanov.catty_catalog.view.columns.TableColumnFactory;
import com.ilya.ivanov.catty_catalog.view.columns.TreeTableColumnFactory;
import com.ilya.ivanov.catty_catalog.view.tabs.TabFactory;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class MainStageController implements StageController {
    @FXML HBox controlButtons;
    @FXML Button addButton;
	@FXML Button removeButton;

	@FXML TextField searchField;
	@FXML TabPane tabPane;

	@FXML StackPane tableStack;

	@FXML VBox workingTreeLayout;
	@FXML TreeTableView<AbstractFileObject> workingTreeL__TreeTable;

	@FXML VBox searchResultsLayout;
	@FXML TableView<AbstractFileObject> searchL__resultTable;
	@FXML Pagination searchL__pagination;

	@FXML Text userNameLetter;
	@FXML Label workingTreeL__statusBar;


	@FXML public void initialize() {
		// search field. ESC handler
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE))
                handleSearchCrossClicked();
        });

        // if search field is empty then show working tree layout
        // instead of searching result layout
        searchField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(0)) {
                workingTreeLayout.setVisible(true);
                searchResultsLayout.setVisible(false);
            }
        });

        // create tabs and attach them to tabPane according to the list of file categories
        for (String category : Model.fileCategories) {
            tabPane.getTabs().add(TabFactory.getInstance(category));
        }

        // deselect tabs. important cause otherwise selected tab
        // will be empty. selection will be fired manually below
        tabPane.getSelectionModel().clearSelection();

        // set new root of working tree when tab selection was changed
        // or search again in the next tab
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (workingTreeL__TreeTable.isVisible())
                workingTreeL__TreeTable.setRoot(Model.getRoot(newValue.getId()));
            if (searchL__resultTable.isVisible())
                handleSearch();
        });

        // manually fired selection
        tabPane.getSelectionModel().selectFirst();

        // Result table settings
        searchL__resultTable.setPlaceholder(new Text("Your search did not match any documents"));

        // creating TableColumns
        for (Map.Entry<String, Columns.ColumnModel> entry : Columns.columnsMap.entrySet())
            searchL__resultTable
                    .getColumns()
                    .add((TableColumn<AbstractFileObject, String>) (new TableColumnFactory()).getColumn(entry.getKey(), entry.getValue()));

        // Working TreeTable settings
        // creating TreeTableColumns
        for (Map.Entry<String, Columns.ColumnModel> entry : Columns.columnsMap.entrySet())
            workingTreeL__TreeTable
                    .getColumns()
                    .add((TreeTableColumn<AbstractFileObject, String>) (new TreeTableColumnFactory()).getColumn(entry.getKey(), entry.getValue()));
	}

	@FXML public void handleSignOut() {
		try {
            StageDriver.setStage("login");
		} catch(Exception e) {
			e.printStackTrace();
		}
		Model.setUser(null);
	}

    /**
     * Executes when Add button was triggered
     */
	@FXML public void handleAddButtonAction() {
	    // calculate the parent element
        // to add new files
        TreeItem<AbstractFileObject> selectedItem = workingTreeL__TreeTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null)
            selectedItem = workingTreeL__TreeTable.getRoot();
        else if (selectedItem.getValue() instanceof FileObject)
            selectedItem = selectedItem.getParent();

        // call file chooser
        List<File> selectedFiles = FileChooserModal.callFileChooser(tabPane.getSelectionModel().getSelectedItem().getId());
		if (selectedFiles != null) {

		    // calculating size of files
            long totalSize = 0;
			for (File file : selectedFiles) {
                totalSize += file.length();
            }

            // quota exceeded
            if (totalSize > Model.getUser().getQuota()) {
                Alert message = new Alert(Alert.AlertType.WARNING);
                message.setTitle("Warning");
                message.setHeaderText("Quota exceeded!");
                message.initOwner(StageDriver.getCurrentStage());
                message.show();
                return;
            }

            // push file to database otherwise
            Model.getUser().subtractQuota(totalSize);

            List<AbstractFileObject> list = new ArrayList<>();
            for (File file : selectedFiles) {
                list.add(new FileObject(file, selectedItem.getValue().getID()));
            }

            DataController.dao.pushFileObjects(list);
            // update model
		}
	}

    /**
     * Executes when Remove button was triggered
     * @param event
     */
	@FXML public void handleRemoveButtonAction(ActionEvent event) {
		tabPane.getSelectionModel().getSelectedItem().getId();
		// deleteRequest
		// render new view
	}

    /**
     * Executes when search field is focused and Enter pressed
     * or when search result layout is visible and tab was changed
     */
	@FXML public void handleSearch() {
	    String searchRequest = searchField.getText();

		if (!searchRequest.isEmpty()) {
            // change tables visibility
            workingTreeLayout.setVisible(false);
            searchResultsLayout.setVisible(true);

            ObservableList<AbstractFileObject> files = Model.getFileObjectsAsList(workingTreeL__TreeTable.getRoot());

            FilteredList<AbstractFileObject> filtered = files
                    .filtered(abstractFileObject ->
                                    abstractFileObject
                                            .getFilename()
                                            .regionMatches(true, 0, searchRequest, 0, searchRequest.length())
                    );

            searchL__resultTable.setItems(filtered);

            // parse pages
        }
	}

	// Executes when close cross in search field was clicked
	public void handleSearchCrossClicked() {
		searchField.setText("");
	}

	@Override
	public void onStageShown() {
        // show first capitalized letter of username in top-right corner
        if (Model.getUser() != null) {
            userNameLetter.setText(String.valueOf(Model.getUser().getName().charAt(0)).toUpperCase());

            // set workspace according to user's permissions
            switch (Model.getUser().getType()) {
                case guest:
                    workingTreeL__statusBar.setText("Read-only mode");
                    controlButtons.setDisable(true);
                    break;
                case user:
                    workingTreeL__statusBar.setText("Your quota for uploading files is " + Model.getUser().getQuota() + " for today");
                    removeButton.setDisable(true);
                    break;
                case admin:
                    workingTreeL__statusBar.setText("Full-capacity mode");
                    break;
            }
        }
    }
}
