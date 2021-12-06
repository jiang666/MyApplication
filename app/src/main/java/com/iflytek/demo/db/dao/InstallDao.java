package com.iflytek.demo.db.dao;

import android.content.Context;

import com.iflytek.demo.db.DatabaseHelper;
import com.iflytek.demo.db.entity.Install;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The utility to operate install table
 */
public class InstallDao {

    private static final String TAG     = InstallDao.class.getSimpleName();
    private static final boolean Debug  = true;

    private DatabaseHelper helper;
    private Dao<Install, Integer> dao;

    /**
     * The constructor
     * @param context The application context
     */
    public InstallDao(Context context) {
        try {
            helper = DatabaseHelper.getInstance(context);
            dao = helper.getInstallDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO CRUD

    /**
     * Add one material record to database
     * @param install The Install object info
     */
    public void addInstall(Install install) {
        if (dao != null)
            try {
                dao.createOrUpdate(install);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * Delete target install
     * @param install The install to be deleted
     * @return
     *  The deleted record count
     */
    public int deleteinstall(Install install) {
        if (dao != null)  {
            try {
                return dao.delete(install);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    /**
     * Query all play from database
     * @return
     *  Program list
     */
    public List<Install> queryForAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all installs which have the same type property
     * @param type The installs version
     * @return The list of installs have the same type
     */
    public List<Install> queryByType(String type) {
        if (dao != null) {
            try {
                return dao.queryForEq(Install.TYPE_FIELD, type);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Query all installs which match the given type
     * @param //type The type column of install table
     * @return
     *  install list which match the given type
     */
    public List<Install> queryByMD5(String md5) {
        if (dao != null) {
            try {
                return dao.queryForEq(Install.MD5_FIELD, md5);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean update(Install m) {
        if (dao != null)  {
            try {
                return dao.update(m) > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
