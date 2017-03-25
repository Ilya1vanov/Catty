package model.browser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Created by Илья on 24.03.2017.
 * Not-instantiated class, used to browse files using desktop utilities and resources,
 * it uses default programs of desktop to open files
 */
public class DesktopBrowser {

    /** Object of users desktop, which allows to open files by its default programs */
    private final static Desktop desktop = Desktop.getDesktop();

    /**
     *  private constructor of class, which implement non-instantiation
     */
    private DesktopBrowser(){}

    /**
     * opens files on users desktop by default programmes, that are associated with files extension
     * @param file - object that represent file
     */
    public static void openFile(File file){
        try {
            desktop.open(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
