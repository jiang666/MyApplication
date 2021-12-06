package com.iflytek.demo.db.dao;

import android.content.Context;

import com.iflytek.demo.db.DatabaseHelper;
import com.iflytek.demo.db.entity.Face;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The utility to operate face table
 */
public class FaceDao {

    private static final String TAG     = FaceDao.class.getSimpleName();
    private static final boolean Debug  = true;

    private DatabaseHelper helper;
    private Dao<Face, Integer> dao;

    /**
     * The constructor
     * @param context The application context
     */
    public FaceDao(Context context) {
        try {
            helper = DatabaseHelper.getInstance(context);
            dao = helper.getFaceDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO CRUD

    /**
     * Add one material record to database
     * @param face The Face object info
     */
    public void addFace(Face face) {
        if (dao != null)
            try {
                dao.createOrUpdate(face);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * Delete target face
     * @param face The face to be deleted
     * @return
     *  The deleted record count
     */
    public int deleteface(Face face) {
        if (dao != null)  {
            try {
                return dao.delete(face);
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
    public List<Face> queryForAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all faces which have the same type property
     * @param type The faces version
     * @return The list of faces have the same type
     */
    public List<Face> queryBycardNumber(String type) {
        if (dao != null) {
            try {
                return dao.queryForEq(Face.CARDNUMBER, type);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public boolean update(Face m) {
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
