package view.context;

import controller.stagecontrollers.MainStageController;
import model.Model;
import model.file.AbstractFileObject;
import model.file.DirectoryObject;
import model.user.AbstractUser;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MenuItem;

/**
 * Custom context menu controls.<br>
 *     Contains user logic. Shows menu items according to user's permissions and
 *     invocation place in View.<br>
 * Created by Илья on 14.03.2017.
 */
public class RowContextMenu extends ContextMenu {
    private final MenuItem previewMI = new MenuItem("Open...");
    private final MenuItem addDirMI = new MenuItem("Add new directory");
    private final MenuItem removeMI = new MenuItem("Remove");
    private final MenuItem renameMI = new MenuItem("Rename");
    private final MenuItem proposalMI = new MenuItem("Send proposal for adding");

    public RowContextMenu(MainStageController controller) {
        setOnShown(event -> {
            IndexedCell<AbstractFileObject> row = (IndexedCell<AbstractFileObject>) ((ContextMenu) event.getSource()).getOwnerNode();
            AbstractUser user = Model.getUser();

            AbstractFileObject fileObject = row.getItem();

            previewMI.setDisable(fileObject == null || fileObject instanceof DirectoryObject);

            if (fileObject != null) {
                renameMI.setDisable(
                        row.isEmpty() ||
                                !user.getName().equals(fileObject.getOwner()) &&
                                fileObject.getOwner() != null);


            removeMI.setDisable(
                    row.isEmpty() ||
                            !user.getName().equals(fileObject.getOwner()) &&
                            !user.hasEditPerm() &&
                            fileObject.getOwner() != null);
            }
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

            proposalMI.setOnAction(event ->
                    controller.handleProposalSending());
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
