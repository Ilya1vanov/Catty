package com.ilya.ivanov.catty_catalog.view.columns;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextAlignment;

/**
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
    protected void setAlignment(TableColumnBase column, TextAlignment alignment) {
        ((TableColumn)column).setCellFactory(param -> {
            TableCell<S, T> cell = new TableCell();

            cell.setTextAlignment(alignment);
            return cell;
        });
    }
}
