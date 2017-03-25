package view.columns;

import model.file.AbstractFileObject;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Pattern "Factory Method". Creates and decorates TableColumns.<br>
 * <br>
 * Created by Илья on 04.03.2017.
 */
public class TableColumnFactory<S, T> extends AbstractColumnFactory {
    @Override
    protected TableColumnBase createColumn(String name) {
        TableColumn<S, T> column = new TableColumn<>(name.substring(0, 1).toUpperCase() + name.substring(1));
        column.setCellValueFactory(new PropertyValueFactory(name));
        return column;
    }

    @Override
    protected void setAlignment(TableColumnBase column, Pos alignment) {
        ((TableColumn)column).setCellFactory(param -> {
            TableCell<AbstractFileObject, String> tc = new TableCell<AbstractFileObject, String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    setText(item);
                    if (empty)
                        setText(null);
                }
            };
            tc.setAlignment(alignment);
            return tc;
        });
    }
}
