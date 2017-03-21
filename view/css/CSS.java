package com.ilya.ivanov.catty_catalog.view.css;

import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Илья on 12.03.2017.
 */
public class CSS {
    private static int curIndex;

    private static final String[] styles = {"highcontrast", "blackOnWhite", "caspian", "modenaDark"};

    private static final HashMap<String, String> stylesheets = new HashMap<>();

    static {
        for (String style : styles) {
            stylesheets.put(style, CSS.class.getResource(style + ".css").toExternalForm());
        }
    }

    // highcontrast is a default
    public static void setNext(Stage stage) {
        if (stage.getScene().getStylesheets().contains(stylesheets.get(styles[curIndex])))
            stage.getScene().getStylesheets().remove(stylesheets.get(styles[curIndex]));
        if (++curIndex >= styles.length)
            curIndex = 0;
        stage.getScene().getStylesheets().add(stylesheets.get(styles[curIndex]));
    }

    public static void setStyle(Stage stage, String name) {
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(stylesheets.get(name));
    }

    public static void setDefaultStyle(Stage stage, String name) {
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(stylesheets.get("highcontrast"));
    }
}
