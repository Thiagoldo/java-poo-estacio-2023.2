package models.dao;

import db.DB;
import models.dao.impl.AssetDaoJdbc;
import models.dao.impl.CategoryDaoJdbc;
import models.dao.impl.SectorDaoJdbc;
import models.dao.impl.UserDaoJdbc;
import models.dao.impl.AssetSectorDaoJdbc;

public class DaoFactory {
    public static UserDAO createUserDAO() {
        return new UserDaoJdbc(DB.getConnection());
    }

    public static SectorDAO createSectorDAO() {
        return new SectorDaoJdbc(DB.getConnection());
    }

    public static CategoryDAO createCategoryDAO() {
        return new CategoryDaoJdbc(DB.getConnection());
    }

    public static AssetDAO createAssetDAO() {
        return new AssetDaoJdbc(DB.getConnection());
    }

    public static AssetSectorDAO createAssetSectorDAO() {
        return new AssetSectorDaoJdbc(DB.getConnection());
    }
}
