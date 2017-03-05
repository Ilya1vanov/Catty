package com.ilya.ivanov.catty_catalog.model;

import com.ilya.ivanov.catty_catalog.controller.DataController;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import com.ilya.ivanov.catty_catalog.model.user.Guest;
import com.ilya.ivanov.catty_catalog.view.columns.ColumnModel;
import javafx.scene.control.TreeItem;

import java.util.*;

/**
 * Created by ivano on 02.03.2017.
 */
public class Model {
    public static AbstractUser user;

    public static final String[] fileCategories = {"documents", "books", "audios", "videos"};

    public static final LinkedHashMap<String, ColumnModel> modelColumnsMap;

    static {
        modelColumnsMap = new LinkedHashMap<>();
        final String[] fileProperties = {"filename", "path", "size"};
        final ColumnModel[] models = {ColumnModel.WIDE, ColumnModel.MEDIUM, ColumnModel.TINY};

        for (int i = 0; i < fileProperties.length; ++i)
            modelColumnsMap.put(fileProperties[i], models[i]);
    }

    private static final HashMap<String, TreeItem<AbstractFileObject>> rootMap;

    static {
        rootMap = new HashMap<>();
        for (String category : fileCategories)
            rootMap.put(category, DataController.dao.pullWorkingTree(category));
    }

    public static TreeItem<AbstractFileObject> getRoot(String rootName) {
        if (!rootMap.containsKey(rootName))
            throw new RuntimeException("Unsupported param: " + rootName);
        return rootMap.get(rootName);
    }
}
