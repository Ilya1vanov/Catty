package view.tabs;


import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * creates new Tab
 * Created by Илья on 04.03.2017.
 */
public class TabFactory {
    public static Tab getInstance(String name) {
        Tab tab = new Tab();
        tab.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
        tab.setId(name);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(22);
        imageView.setFitHeight(22);
        imageView.setImage(new Image(TabFactory.class.getResourceAsStream("../../pics/" + name + "-icon.png")));
        tab.setGraphic(imageView);
        return tab;
    }
}