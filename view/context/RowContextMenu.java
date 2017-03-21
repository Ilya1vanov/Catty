package com.ilya.ivanov.catty_catalog.view.context;

import com.ilya.ivanov.catty_catalog.controller.Controller;
import com.ilya.ivanov.catty_catalog.controller.stagecontrollers.MainStageController;
import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.file.DirectoryObject;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MenuItem;

/**
 * Created by Илья on 14.03.2017.
 */
public class RowContextMenu extends ContextMenu {
    private MenuItem previewMI = new MenuItem("Preview");
    private MenuItem addDirMI = new MenuItem("Add new directory");
    private MenuItem removeMI = new MenuItem("Remove");
    private MenuItem renameMI = new MenuItem("Rename");
    private MenuItem proposalMI = new MenuItem("Send proposal for adding");

    public RowContextMenu(MainStageController controller) {
        setOnShown(event -> {
            IndexedCell<AbstractFileObject> row = (IndexedCell<AbstractFileObject>) ((ContextMenu) event.getSource()).getOwnerNode();
            AbstractUser user = Model.getUser();

            AbstractFileObject fileObject = row.getItem();

            previewMI.setDisable(fileObject == null || fileObject instanceof DirectoryObject);

            renameMI.setDisable(
                    row.isEmpty() ||
                            !user.getName().equals(fileObject.getOwner()) &&
                            fileObject.getOwner() != null);

            removeMI.setDisable(
                    row.isEmpty() ||
                            !user.getName().equals(fileObject.getOwner()) &&
                            !user.hasEditPerm() &&
                            fileObject.getOwner() != null);
        });

        addDirMI.disableProperty().bind(previewMI.disableProperty().not());

        if (controller != null) {
            previewMI.setOnAction(event ->
                    controller.handleOpen((IndexedCell) ((MenuItem) event.getSource()).getParentPopup().getOwnerNode()));

            addDirMI.setOnAction(event ->
                controller.handleAddDirectory((IndexedCell) ((MenuItem) event.getSource()).getParentPopup().getOwnerNode()));

            removeMI.setOnAction(event ->
                    controller.handleRemove());

            renameMI.setOnAction(event ->
                    controller.handleRename((IndexedCell) ((MenuItem) event.getSource()).getParentPopup().getOwnerNode()));
        }

        Model.userProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getItems().clear();
                getItems().add(previewMI);
                if (newValue.hasWritePerm()) {
                    getItems().addAll(addDirMI, removeMI, renameMI);
                }
                else {
                    getItems().add(proposalMI);
                }
            }
        });
    }
}
