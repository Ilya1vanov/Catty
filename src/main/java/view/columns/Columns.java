package view.columns;


import javafx.geometry.Pos;

import java.util.LinkedHashMap;

/**
 * Works with Columns in in TreeTableView and TableView. Stores &lt name, model &gt pairs.
 * Created by Илья on 06.03.2017.
 */
public class Columns {
    private static final LinkedHashMap<String, ColumnModel> columnsMap;
    private static final String[] columnNames = {"name", "owner", "size", "uploaded"};

    static {
        columnsMap = new LinkedHashMap<>();

        final ColumnModel[] models = {ColumnModel.WIDE, ColumnModel.MEDIUM, ColumnModel.TINY, ColumnModel.TINY};

        for (int i = 0; i < columnNames.length; ++i)
            columnsMap.put(columnNames[i], models[i]);
    }

    public static LinkedHashMap<String, ColumnModel> getColumnsMap() {
        return columnsMap;
    }

    /**
     * View models of the Columns in TreeTableView and TableView.
     * Created by Илья on 04.03.2017.
     */
    public enum ColumnModel {
        TINY(100.0, 100.0, 100.0, Pos.CENTER), MEDIUM(300.0, 350.0, 550.0, Pos.CENTER_LEFT), WIDE(500.0, 550.0, 5000.0, Pos.CENTER_LEFT);

        private final double minWidth;
        private final double prefWidth;
        private final double maxWidth;
        private final Pos alignment;

        ColumnModel(double minWidth, double prefWidth, double maxWidth, Pos alignment) {
            this.minWidth = minWidth;
            this.prefWidth = prefWidth;
            this.maxWidth = maxWidth;
            this.alignment = alignment;
        }

        public double getMinWidth() {
            return minWidth;
        }

        public double getPrefWidth() {
            return prefWidth;
        }

        public double getMaxWidth() {
            return maxWidth;
        }

        public Pos getAlignment() {
            return alignment;
        }
    }

    /**
     * Returns array of names of the currently stored columns.
     * @return Array of names of the currently stored columns.
     */
    public static String[] getColumnNames() {
        return columnNames;
    }
}
