package controller;

import controller.dao.DAO;
import controller.dao.DAOSQLite;
import model.file.AbstractFileObject;
import model.user.AbstractUser;

/**
 * Data storage controller.<br>
 *     Can be replaced with "Adapter" pattern.
 *     <br>
 * Created by ivanov on 03.03.2017.
 */
public class DataController {
    public static final DAO<AbstractFileObject, AbstractUser> dao = new DAOSQLite();
}
