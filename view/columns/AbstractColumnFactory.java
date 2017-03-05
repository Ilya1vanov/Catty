package com.ilya.ivanov.catty_catalog.view.columns;

import javafx.scene.control.TableColumnBase;
import javafx.scene.text.TextAlignment;

/**
 * Created by Илья on 04.03.2017.
 */
public abstract class AbstractColumnFactory {
    public final TableColumnBase getColumn(String name, ColumnModel model) {
        TableColumnBase column = createColumn(name);
        column.setId(name + "Col");
//        setAlignment(column, model.getTextAlignment());
        setWidthBehavior(column, model.getMinWidth(), model.getPrefWidth(), model.getMaxWidth());
        return column;
    }

    protected abstract TableColumnBase createColumn(String name);

    protected abstract void setAlignment(TableColumnBase column, TextAlignment alignment);

    protected final void setWidthBehavior(TableColumnBase column, double minWidth, double prefWith, double maxWidth) {
        column.setMinWidth(minWidth);
        column.setPrefWidth(prefWith);
        column.setMaxWidth(maxWidth);
    }
}
