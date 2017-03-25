package view.css;

import javafx.stage.Stage;
import java.util.HashMap;

/**
 * CSSDriver manage stylesheets.
 * Created by Илья on 12.03.2017.
 */
public class CSSDriver {
    private static int curIndex;

    private static final String[] styles = {"highcontrast", "blackOnWhite", "caspian", "modenaDark"};

    private static final HashMap<String, String> stylesheets = new HashMap<>();

    static {
        for (String style : styles) {
            stylesheets.put(style, "css/" + style + ".css");
        }
    }

    /**
     * Set next stylesheet from CSSDriver.getStyles() array.
     * @param stage Stage object.
     */
    public static void setNext(Stage stage) {
        if (stage.getScene().getStylesheets().contains(stylesheets.get(styles[curIndex])))
            stage.getScene().getStylesheets().remove(stylesheets.get(styles[curIndex]));
        if (++curIndex >= styles.length)
            curIndex = 0;
        stage.getScene().getStylesheets().add(stylesheets.get(styles[curIndex]));
    }

    /**
     *
     * @param stage Stage object.
     * @param name Name of stylesheet.
     */
    public static void setStyle(Stage stage, String name) {
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(stylesheets.get(name));
    }

    /**
     * Reset stylesheet to default.
     * @param stage Stage object.
     */
    public static void setDefaultStyle(Stage stage) {
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(stylesheets.get("highcontrast"));
    }

    /**
     * Returns names of all available stylesheets.
     * @return names of all available stylesheets.
     */
    public static String[] getStyles() {
        return styles;
    }
 }
