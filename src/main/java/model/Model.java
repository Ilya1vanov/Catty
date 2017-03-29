package model;

import controller.DataController;
import model.file.AbstractFileObject;
import model.file.DirectoryObject;
import model.user.AbstractUser;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TreeItem;
import org.apache.log4j.Logger;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * MVC model.
 * Created by ivanov on 02.03.2017.
 */
public class Model {
    /* default logger */
    private static final Logger log = Logger.getLogger(Model.class);

    /* categories of stored files */
    public static final String[] fileCategories = {"documents", "books", "audios", "videos"};

    /* user object property to observe user changing */
    private static final ObjectProperty<AbstractUser> user = new SimpleObjectProperty<>();

    /* currently used root */
    private static String currentRoot;

    /* root map name -> TreeItem; observable to perform updating */
    private static final ObservableMap<String, TreeItem<AbstractFileObject>> rootMap;

    /* Validator to perform some kind of validation */
    private static final Validator validator;

    /* store previous search results to perform page displaying */
    private static ObservableList<AbstractFileObject> searchResults;

    /* validator initialize */
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /* root map initialize */
    static {
        HashMap<String, TreeItem<AbstractFileObject>> hashMap = new HashMap<>();
        for (String category : fileCategories)
            hashMap.put(category, DataController.dao.pullWorkingTree(category));

        rootMap = new ObservableMapWrapper<>(hashMap);

        // debug
        rootMap.addListener(new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable observable) {
                log.info("RootMap invalid! Updating...");
            }
        });
    }

    public static Validator getValidator() {
        return validator;
    }

    /**
     * Returns root of the currently shown file hierarchy.
     * @return Root of the currently shown file hierarchy.
     */
    public static TreeItem<AbstractFileObject> getCurrentRoot() {
        return rootMap.get(currentRoot);
    }

    /**
     * Returns name root of the currently shown file hierarchy.
     * @return Root name of the currently shown file hierarchy.
     */
    public static String getCurrentRootName() {
        return currentRoot;
    }

    public static ObservableList<AbstractFileObject> getSearchResults() {
        return searchResults;
    }

    public static void setSearchResults(ObservableList<AbstractFileObject> searchResults) {
        Model.searchResults = searchResults;
    }

    /**
     * Sets current root in Model.
     * @param currentRoot Name of new root.
     */
    public static void setRoot(String currentRoot) {
        Model.currentRoot = currentRoot;
    }

    public static ObjectProperty<AbstractUser> userProperty() {
        return user;
    }

    /**
     * Returns current object of storage userObj.
     * @return Current object of storage userObj.
     */
    public static AbstractUser getUser() {
        return user.get();
    }

    /**
     * Sets current object of storage userObj.
     * @param user Object of new storage userObj.
     */
    public static void setUser(AbstractUser user) {
        Model.user.set(user);
    }

    /**
     * Returns root item of file hierarchy according to given root name.
     * @param rootName Name of file hierarchy.
     * @return Root item of file hierarchy.
     */
    public static TreeItem<AbstractFileObject> getRoot(String rootName) {
        if (!rootMap.containsKey(rootName))
            throw new RuntimeException("Unsupported param: " + rootName);
        return rootMap.get(rootName);
    }

    /**
     * Returns list of files (not directories) of the current root.
     * @return List of file represented in given root.
     */
    public static ObservableList<AbstractFileObject> getFileObjectsAsList() {
        List<AbstractFileObject> list = new ArrayList<>();

        recursiveTraversal(list, rootMap.get(currentRoot));

        return new ObservableListWrapper<>(list);
    }

    /**
     * Recursively collects all items than are children of given TreeItem. And appends them to the list.
     * @param list List to append.
     * @param parent Current root TreeItem.
     */
    private static void recursiveTraversal(List<AbstractFileObject> list, TreeItem<AbstractFileObject> parent) {
        for (TreeItem<AbstractFileObject> item : parent.getChildren()) {
            if (item.getValue() instanceof DirectoryObject)
                recursiveTraversal(list, item);
            else
                list.add(item.getValue());
        }
    }

    /**
     * Update current root.
     */
    public static void update() {
        rootMap.put(currentRoot, DataController.dao.pullWorkingTree(currentRoot));
    }
}
