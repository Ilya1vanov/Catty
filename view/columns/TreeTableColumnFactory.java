package com.ilya.ivanov.catty_catalog.view.columns;

import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
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
    protected void setAlignment(TableColumnBase column, TextAlignment alignment) {
        ((TreeTableColumn) column).setCellFactory(param -> {
            TreeTableCell<S, T> cell = new TreeTableCell();
            cell.setTextAlignment(alignment);
            return cell;
        });
    }
}
