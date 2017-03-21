package com.ilya.ivanov.catty_catalog.view.columns;

import javafx.geometry.Pos;
import javafx.scene.control.TableColumnBase;
import javafx.scene.text.TextAlignment;

/**
 * Created by Илья on 04.03.2017.
 */
public abstract class AbstractColumnFactory {
    public final TableColumnBase getColumn(String name, Columns.ColumnModel model) {
        TableColumnBase column = createColumn(name);
        column.setId(name + "Col");
        setAlignment(column, model.getAlignment());
        setWidthBehavior(column, model.getMinWidth(), model.getPrefWidth(), model.getMaxWidth());
        return column;
    }

    protected abstract TableColumnBase createColumn(String name);

    protected abstract void setAlignment(TableColumnBase column, Pos alignment);

    protected final void setWidthBehavior(TableColumnBase column, double minWidth, double prefWith, double maxWidth) {
        column.setMinWidth(minWidth);
        column.setPrefWidth(prefWith);
        column.setMaxWidth(maxWidth);
    }
}
