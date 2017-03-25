package view.chooser;

import model.Model;
import view.View;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Modal Window. Called when file choosing occurs.<br>
 *     Map file and their extensions.<br>
 *     <br>
 * Created by Илья on 05.03.2017.
 */
public class FileChooserModal {
    private static final HashMap<String, FileChooser.ExtensionFilter> extensionsMap = new HashMap<>();

    static {
        final ArrayList[] extensions = new ArrayList[]{
                new ArrayList<>(Arrays.asList("*.pdf", "*.doc", "*.docx","*.rtf")),
                new ArrayList<>(Arrays.asList("*.txt", "*.pdf")),
                new ArrayList<>(Arrays.asList("*.mp3", "*.flac", "*.wv", "*.wav", "*.wma")),
                new ArrayList<>(Arrays.asList("*.mp4", "*.mkv", "*.avi", "*.mov", "*.webm"))
        };
        for (int i = 0; i < Model.fileCategories.length; ++i) {
            extensionsMap.put(Model.fileCategories[i], new FileChooser.ExtensionFilter(
                    Model.fileCategories[i].substring(0, 1).toUpperCase() + Model.fileCategories[i].substring(1),
                    extensions[i]));
        }
        extensionsMap.put("all", new FileChooser.ExtensionFilter("All files", "*.*"));
    }

    public static List<File> callFileChooser(String category) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select files");
        fileChooser.getExtensionFilters().addAll(extensionsMap.get(category));
        return fileChooser.showOpenMultipleDialog(View.getCurrentStage());
    }
}
