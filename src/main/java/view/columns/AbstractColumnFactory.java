package view.columns;

import javafx.geometry.Pos;
import javafx.scene.control.TableColumnBase;


/**
 * Pattern "Factory Method".<br>
 * <br>
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

    private void setWidthBehavior(TableColumnBase column, double minWidth, double prefWith, double maxWidth) {
        column.setMinWidth(minWidth);
        column.setPrefWidth(prefWith);
        column.setMaxWidth(maxWidth);
    }
}
