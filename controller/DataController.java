package com.ilya.ivanov.catty_catalog.controller;

import com.ilya.ivanov.catty_catalog.controller.dao.DAO;
import com.ilya.ivanov.catty_catalog.controller.dao.DAOSQLite;
import com.ilya.ivanov.catty_catalog.model.file.AbstractFileObject;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;

/**
 * Created by ivano on 03.03.2017.
 */
public class DataController {
    public static DAO<AbstractFileObject, AbstractUser> dao = new DAOSQLite();
}
