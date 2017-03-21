package com.ilya.ivanov.catty_catalog.view.columns;

import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 * Created by Илья on 04.03.2017.
 */
public class TreeTableColumnFactory<S, T> extends AbstractColumnFactory {
    @Override
    protected TableColumnBase createColumn(String name) {
        TreeTableColumn<S, T> column = new TreeTableColumn<>(name.substring(0, 1).toUpperCase() + name.substring(1));
        column.setCellValueFactory(new TreeItemPropertyValueFactory(name));
        return column;
    }

    @Override
    protected void setAlignment(TableColumnBase column, Pos alignment) {
        ((TreeTableColumn) column).setCellFactory(param -> {
            TreeTableCell<AbstractFileObject, String> tc = new TreeTableCell<AbstractFileObject, String>(){
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
