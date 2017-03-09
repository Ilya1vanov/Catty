package com.ilya.ivanov.catty_catalog.model;

import com.ilya.ivanov.catty_catalog.controller.DataController;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.file.DirectoryObject;
import com.ilya.ivanov.catty_catalog.model.file.FileObject;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import com.ilya.ivanov.catty_catalog.view.columns.Columns;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ivano on 02.03.2017.
 */
public class Model {
    private static AbstractUser user;

    private static String currentRoot;

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

    public static void setCurrentRoot(String currentRoot) {
        Model.currentRoot = currentRoot;
    }

    private static final HashMap<String, TreeItem<AbstractFileObject>> rootMap;

    public static final String[] fileCategories = {"documents", "books", "audios", "videos"};

    static {
        rootMap = new HashMap<>();
        for (String category : fileCategories)
            rootMap.put(category, DataController.dao.pullWorkingTree(category));
    }

    public static AbstractUser getUser() {
        return user;
    }

    public static void setUser(AbstractUser user) {
        Model.user = user;
    }

    public static TreeItem<AbstractFileObject> getRoot(String rootName) {
        if (!rootMap.containsKey(rootName))
            throw new RuntimeException("Unsupported param: " + rootName);
        return rootMap.get(rootName);
    }

//    public static String getCurrentRootName() {
//        return currentRoot;
//    }
//
//    public static TreeItem<AbstractFileObject> getCurrentRoot() {
//        return rootMap.get(currentRoot);
//    }

    public static ObservableList<AbstractFileObject> getFileObjectsAsList(TreeItem<AbstractFileObject> root) {
        List<AbstractFileObject> list = new ArrayList<>();

        recursiveTraversal(list, root);

        return new ObservableListWrapper<>(list);
    }

    private static void recursiveTraversal(List<AbstractFileObject> list, TreeItem<AbstractFileObject> parent) {
        for (TreeItem<AbstractFileObject> item : parent.getChildren()) {
            if (item.getValue() instanceof DirectoryObject)
                recursiveTraversal(list, item);
            else
                list.add(item.getValue());
        }
    }
}
