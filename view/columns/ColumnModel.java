package com.ilya.ivanov.catty_catalog.view.columns;

import javafx.scene.text.TextAlignment;

/**
 * Created by Илья on 04.03.2017.
 */
public enum ColumnModel {
    TINY(150.0, 150.0, 250.0, TextAlignment.CENTER), MEDIUM(300.0, 350.0, 550.0, TextAlignment.LEFT), WIDE(500.0, 550.0, 5000.0, TextAlignment.LEFT);

    protected double minWidth;
    protected double prefWidth;
    protected double maxWidth;
    protected TextAlignment textAlignment;

    ColumnModel(double minWidth, double prefWidth, double maxWidth, TextAlignment alignment) {
        this.minWidth = minWidth;
        this.prefWidth = prefWidth;
        this.maxWidth = maxWidth;
        this.textAlignment = alignment;
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

    public TextAlignment getTextAlignment() {
        return textAlignment;
    }
}
