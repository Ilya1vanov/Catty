package com.ilya.ivanov.catty_catalog.view.columns;


import javafx.geometry.Pos;

import java.util.LinkedHashMap;

/**
 * Created by Илья on 06.03.2017.
 */
public class Columns {
    public static final LinkedHashMap<String, ColumnModel> columnsMap;

    static {
        columnsMap = new LinkedHashMap<>();
        final String[] columnNames = {"name", "owner", "size", "uploaded"};
        final ColumnModel[] models = {ColumnModel.WIDE, ColumnModel.MEDIUM, ColumnModel.TINY, ColumnModel.TINY};

        for (int i = 0; i < columnNames.length; ++i)
            columnsMap.put(columnNames[i], models[i]);
    }

    /**
     * Created by Илья on 04.03.2017.
     */
    public enum ColumnModel {
        TINY(100.0, 100.0, 100.0, Pos.CENTER), MEDIUM(300.0, 350.0, 550.0, Pos.CENTER_LEFT), WIDE(500.0, 550.0, 5000.0, Pos.CENTER_LEFT);

        protected double minWidth;
        protected double prefWidth;
        protected double maxWidth;
        protected Pos alignment;

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
}
