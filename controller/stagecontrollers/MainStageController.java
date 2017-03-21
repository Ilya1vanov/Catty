package com.ilya.ivanov.catty_catalog.controller.stagecontrollers;

import com.ilya.ivanov.catty_catalog.controller.DataController;
import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.file.DirectoryObject;
import com.ilya.ivanov.catty_catalog.model.file.FileObject;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import com.ilya.ivanov.catty_catalog.view.chooser.FileChooserModal;
import com.ilya.ivanov.catty_catalog.view.columns.Columns;
import com.ilya.ivanov.catty_catalog.view.View;
import com.ilya.ivanov.catty_catalog.view.columns.TableColumnFactory;
import com.ilya.ivanov.catty_catalog.view.columns.TreeTableColumnFactory;
import com.ilya.ivanov.catty_catalog.view.context.RowContextMenu;
import com.ilya.ivanov.catty_catalog.view.css.CSS;
import com.ilya.ivanov.catty_catalog.view.tabs.TabFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainStageController implements StageController {
    @FXML VBox root;

    @FXML HBox controlButtons;
    @FXML Pane addButtonWrapper;
    @FXML Pane removeButtonWrapper;
    @FXML Button addButton;
	@FXML Button removeButton;

    @FXML Text userNameLetter;

	@FXML TextField searchField;
	@FXML TabPane tabPane;

	@FXML VBox workingTreeLayout;
	@FXML TreeTableView<AbstractFileObject> workingTreeL__TreeTable;
    @FXML Label workingTreeL__statusBar;

	@FXML VBox searchResultsLayout;
	@FXML TableView<AbstractFileObject> searchL__resultTable;
    @FXML AnchorPane searchL__statusBar;
    @FXML Text searchL__resultText;
	@FXML Pagination searchL__pagination;

    private ContextMenu rowContext = new RowContextMenu(this);

    private Alert permissionsError = new Alert(Alert.AlertType.ERROR);
    private Alert quotaWarning = new Alert(Alert.AlertType.WARNING);
    private Alert removeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

    private Dialog<String> dialog = new Dialog<>();


	@FXML public void initialize() {
	    root.addEventHandler(KeyEvent.ANY, event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case F5:
                    sequentialUpdate();
                    break;
            }
        });

        initializeHeaderLayout();

        initializeSearchLayout();

        initializeTabPane();

        initializeWorkingLayout();

        initializeSearchingLayout();

        initializeDialogs();
	}

    private void initializeHeaderLayout() {
        initializeControls();

        initializeUserBlock();
    }

	private void initializeControls() {
        Model.userProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // controls logic
                controlButtons.setDisable(!Model.getUser().hasWritePerm());
            }
        });
    }

    private void initializeUserBlock() {
        Model.userProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // show first capitalized letter of username in top-right corner
                String firstCapitalLetterOfUsername = String.valueOf(Model.getUser().getName().charAt(0)).toUpperCase();
                userNameLetter.setText(firstCapitalLetterOfUsername);
            }
        });
    }

	private void initializeSearchLayout() {
        // search field. ESC handler
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE))
                handleSearchCrossClicked();
        });

        // bind layouts visibility
        searchResultsLayout.visibleProperty().bind(workingTreeLayout.visibleProperty().not());
    }

    private void initializeTabPane() {
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
            if (newValue != null) {
                Model.setRoot(newValue.getId());
                workingTreeL__TreeTable.setRoot(Model.getCurrentRoot());
                if (searchResultsLayout.isVisible())
                    handleSearch();
            }
        });

        // manually fired selection
        tabPane.getSelectionModel().selectFirst();

        // arrows
//        root.addEventHandler(new EventHandler(KeyEvent event) {
//
//        });
    }

    private void initializeWorkingLayout() {
        initializeWorkingTreeTable();

        initializeWorkingStatusBar();
    }

    private void initializeWorkingTreeTable() {
        // Working table settings
        workingTreeL__TreeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        workingTreeL__TreeTable.setFocusTraversable(false);

        // creating TreeTableColumns
        for (Map.Entry<String, Columns.ColumnModel> entry : Columns.columnsMap.entrySet())
            workingTreeL__TreeTable
                    .getColumns()
                    .add((TreeTableColumn<AbstractFileObject, String>) (new TreeTableColumnFactory()).getColumn(entry.getKey(), entry.getValue()));

        // set buttons disable logic in working tree layout
        workingTreeL__TreeTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<AbstractFileObject>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<AbstractFileObject>> c) {
                addButton.setDisable(c.getList().size() > 1);
                removeButton.setDisable(c.getList().size() == 0);
            }
        });

        // row factory. open file on double click. deselect on click in empty row
        workingTreeL__TreeTable.setRowFactory(param -> {
            TreeTableRow<AbstractFileObject> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && row.isEmpty()) {
                    ((TreeTableRow<AbstractFileObject>)event.getSource()).getTreeTableView().getSelectionModel().clearSelection();
                }
                if (event.getClickCount() == 2 && (!row.isEmpty()) && row.getItem() instanceof FileObject) {
                    AbstractFileObject rowData = row.getItem();
                    System.out.println(rowData);
                }
            });

            row.setOnContextMenuRequested(event -> {
                TreeTableRow<AbstractFileObject> source = (TreeTableRow<AbstractFileObject>) event.getSource();
                    rowContext.show(source, event.getScreenX(), event.getScreenY());
            });
            return row;
        });

        // set live preview popup logic
        workingTreeL__TreeTable.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE) && Model.getUser().hasWritePerm())
                handleRemove();
        });

        // hide root element
        workingTreeL__TreeTable.setShowRoot(false);
    }

    private void initializeWorkingStatusBar() {
        Model.userProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (Model.getUser().getType()) {
                    case "guest":
                        workingTreeL__statusBar.setText("Read-only mode");
                        break;
                    case "user":
                        workingTreeL__statusBar.setText("Your quota for uploading files is " + Model.getUser().getHumanReadableQuota() + " for today");
                        Model.getUser().quotaProperty().addListener(
                                (obs, oldVal, newVal) ->
                                        workingTreeL__statusBar.setText("Your quota for uploading files is " + Model.getUser().getHumanReadableQuota() + " for today")
                        );
                        break;
                    case "admin":
                        workingTreeL__statusBar.setText("Full-capacity mode");
                        break;
                }
            }
        });
    }

    private void initializeSearchingLayout() {
        // Result table settings
        searchL__resultTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        searchL__resultTable.setPlaceholder(new Text("Your search did not match any documents"));
//        searchL__resultTable.setFocusTraversable(false);

        // creating TableColumns
        for (Map.Entry<String, Columns.ColumnModel> entry : Columns.columnsMap.entrySet())
            searchL__resultTable
                    .getColumns()
                    .add((TableColumn<AbstractFileObject, String>) (new TableColumnFactory()).getColumn(entry.getKey(), entry.getValue()));

        // pagination index change listener
        searchL__pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int fromIndex = newValue.intValue() * View.rowsPerPage;
            int toIndex = Math.min(fromIndex + View.rowsPerPage, Model.getSearchResults().size());
            searchL__resultTable.setItems(FXCollections.observableArrayList(
                    Model.getSearchResults().subList(fromIndex, toIndex)));
            searchL__resultText.setText("Results on this page: " + String.valueOf(toIndex - fromIndex));
        });

        // set buttons disable logic in search result layout
        searchL__resultTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<AbstractFileObject>() {
            @Override
            public void onChanged(Change<? extends AbstractFileObject> c) {
                removeButton.setDisable(c.getList().size() == 0);
            }
        });

        // Disable add button if searching layout is visible
        searchResultsLayout.visibleProperty().addListener((observable, oldValue, newValue) -> {
            addButtonWrapper.setDisable(newValue);
        });

        // same logic as a working tree table has
        searchL__resultTable.setRowFactory(param -> {
            TableRow<AbstractFileObject> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && row.isEmpty()) {
                    ((TableRow<AbstractFileObject>)event.getSource()).getTableView().getSelectionModel().clearSelection();
                }
                if (event.getClickCount() == 2 && (!row.isEmpty()) && row.getItem() instanceof FileObject) {
                    AbstractFileObject rowData = row.getItem();
                    System.out.println(rowData);
                }
            });
            row.setOnContextMenuRequested(event -> {
                TableRow<AbstractFileObject> source = (TableRow<AbstractFileObject>) event.getSource();
                if (!source.isEmpty())
                    rowContext.show(source, event.getScreenX(), event.getScreenY());
            });
            return row;
        });
    }

    private void initializeDialogs() {
        permissionsError.setTitle("Error");
        permissionsError.setHeaderText("Permission denied!");
        permissionsError.initModality(Modality.APPLICATION_MODAL);
        // hardcode
        permissionsError.initOwner(View.getStage("main"));

        quotaWarning.setTitle("Warning");
        quotaWarning.setHeaderText("Quota exceeded!");
        quotaWarning.initModality(Modality.APPLICATION_MODAL);
        // hardcode
        quotaWarning.initOwner(View.getStage("main"));

        removeConfirmation.setTitle("Remove");
        removeConfirmation.setHeaderText("Confirm deletion!");
        removeConfirmation.setContentText("Are you sure?");
        removeConfirmation.initModality(Modality.APPLICATION_MODAL);
        // hardcode
        removeConfirmation.initOwner(View.getStage("main"));


        dialog.setHeaderText(null);
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField textField = new TextField();
        textField.setId("dialogTextField");
        textField.setPromptText("Name");
        grid.add(textField, 1, 0);

        Label label = new Label("");
        label.setId("dialogLabel");
        grid.add(label, 0, 0);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(textField::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return textField.getText();
            }
            return null;
        });
    }

	@FXML public void handleSignOut() {
		try {
            View.hideAllAndShow("login");
		} catch(Exception e) {
			e.printStackTrace();
		}
		Model.setUser(null);
	}

    /**
     * Executes when Add button was triggered
     */
	@FXML public void handleAddFiles() {
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
                quotaWarning.initOwner(View.getCurrentStage());
                quotaWarning.show();
                return;
            }

            // push file to database otherwise
            Model.getUser().subtractQuota(totalSize);

            // sequential updating
            for (File file : selectedFiles)
                DataController.dao.pushFileObject(new FileObject(file, selectedItem.getValue().getID()), file.getPath());

            sequentialUpdate();

//            MailDriver.sendReport(selectedFiles);
        }
	}

    /**
     * Executes when Add new dir context menu clause fired.
     * @param source TableRow where context menu was invoked.
     */
    public void handleAddDirectory(IndexedCell<AbstractFileObject> source) {
        // Create the custom dialog.
        dialog.setTitle("Add directory");

        GridPane pane = (GridPane) dialog.getDialogPane().getContent();
        ((TextField)pane.lookup("#dialogTextField")).setText("");
        ((Label)pane.lookup("#dialogLabel")).setText("Directory name: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            AbstractFileObject item = source.getItem();
            AbstractFileObject directoryObject = new DirectoryObject(item == null ? Model.getCurrentRoot().getValue().getID() : item.getID(), result.get());
            DataController.dao.pushFileObject(directoryObject, null);

            // sequential updating
            sequentialUpdate();
        }
    }

    /**
     * Executes when Remove button was triggered
     */
	@FXML public void handleRemove() {
        AbstractUser user = Model.getUser();
	    if (!user.hasWritePerm())
	        return;

        Optional<ButtonType> result = removeConfirmation.showAndWait();
        List<TreeItem<AbstractFileObject>> selectedTreeItems = null;
        List<AbstractFileObject> selectedItems;

        if (result.get() == ButtonType.OK) {
            if (workingTreeLayout.isVisible()) {
                selectedTreeItems = workingTreeL__TreeTable.getSelectionModel().getSelectedItems();
                selectedItems = selectedTreeItems.stream().map(TreeItem::getValue).collect(Collectors.toList());
            } else {
                selectedItems = searchL__resultTable.getSelectionModel().getSelectedItems();
            }

            // permissions check
            if (!user.hasEditPerm()) {
                // if not empty directory was selected
                if (selectedTreeItems != null) {
                    Optional<TreeItem<AbstractFileObject>> firstNotEmptyDirectory = selectedTreeItems
                            .stream()
                            .filter(abstractFileObjectTreeItem ->
                                    !abstractFileObjectTreeItem
                                    .getChildren()
                                    .isEmpty())
                            .findFirst();
                    if (firstNotEmptyDirectory.isPresent()) {
                        permissionsError.setContentText("Directory \"" + firstNotEmptyDirectory.get().getValue().getName() + "\" is not empty.");

                        permissionsError.show();
                        return;
                    }
                }

                // if user doesn't own one of selected files
                Stream<String> notUserFiles = selectedItems
                        .stream()
                        .map(AbstractFileObject::getOwner)
                        .filter(s -> s != null && !s.equals(user.getName()));
                if (notUserFiles.count() != 0) {
                    permissionsError.setContentText("You can't remove other user's files.");

                    permissionsError.show();
                    return;
                }
                System.gc();
            }

            // sequential updating
            DataController.dao.deleteFileObjects(selectedItems);

            sequentialUpdate();
        }
	}

    /**
     * Executes when search field is focused and Enter pressed
     * or when search result layout is visible and tab was changed
     */
	@FXML public void handleSearch() {
	    String searchRequest = searchField.getText();

		if (!searchRequest.isEmpty()) {
            // change layouts visibility
            workingTreeLayout.setVisible(false);

            ObservableList<AbstractFileObject> files = Model.getFileObjectsAsList();

            ObservableList<AbstractFileObject> searchResult = files
                    .filtered(abstractFileObject ->
                                    abstractFileObject
                                            .getName()
                                            .toLowerCase().contains(searchRequest.toLowerCase())
                    );
            Model.setSearchResults(searchResult);

            if (searchResult.size() <= View.rowsPerPage) {
                searchL__resultTable.refresh();
                searchL__resultTable.setItems(searchResult);
                searchL__pagination.setVisible(false);
                searchL__resultText.setText("Results on this page: " + searchResult.size());
            }
            else {
                searchL__pagination.setPageCount(searchResult.size() / View.rowsPerPage + 1);
                searchL__pagination.setCurrentPageIndex(1);
                searchL__pagination.setCurrentPageIndex(0);
                searchL__pagination.setVisible(true);
            }
        }
	}

    /**
     * Executes when close cross in search field was clicked
     */
	public void handleSearchCrossClicked() {
		searchField.setText("");
        workingTreeLayout.setVisible(true);
	}

    /**
     * Executes when rename context menu item was selected.
     * @param source TableRow where context menu was invoked.
     */
	public void handleRename(IndexedCell<AbstractFileObject> source) {
        // Create the custom dialog.
        dialog.setTitle("Rename");

        AbstractFileObject item = source.getItem();

        GridPane pane = (GridPane) dialog.getDialogPane().getContent();
        ((TextField)pane.lookup("#dialogTextField")).setText(item.getName());
        ((Label)pane.lookup("#dialogLabel")).setText("New name: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            AbstractFileObject clone = item.clone();
            String name = result.get();

            clone.setName(name);
            if (DataController.dao.update(clone))
                item.setName(name);


            // parallel updating
            parallelUpdate();
        }

    }

    /**
     *
     * @param source TableRow where context menu was invoked.
     */
    public void handlePreview(IndexedCell<AbstractFileObject> source) {
        System.out.println(source.getItem());
        Image image = new Image(DataController.dao.pullFile(source.getItem()));
        ImageView imageView = new ImageView(image);

        root.getChildren().add(imageView);
    }

	@Override
	public void onStageShown() {
        // clear searching field
        searchField.setText("");

        // clear selections model
        workingTreeL__TreeTable.getSelectionModel().clearSelection();
    }

    /**
     * Executes when change theme button was clicked
     */
    @FXML public void handleChangeTheme() {
        CSS.setNext(View.getCurrentStage());
    }

    private void parallelUpdate() {
        workingTreeL__TreeTable.refresh();
        if (searchResultsLayout.isVisible())
            handleSearch();
    }

    private void sequentialUpdate() {
        Model.update();
        workingTreeL__TreeTable.setRoot(Model.getCurrentRoot());
    }
}
