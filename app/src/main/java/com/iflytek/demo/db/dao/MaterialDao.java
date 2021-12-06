package com.iflytek.demo.db.dao;

import android.content.Context;

import com.iflytek.demo.db.DatabaseHelper;
import com.iflytek.demo.db.entity.Material;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The utility to operate materials table
 */
public class MaterialDao {

    private static final String TAG     = MaterialDao.class.getSimpleName();
    private static final boolean Debug  = true;

    private DatabaseHelper helper;
    private Dao<Material, Integer> dao;

    /**
     * The constructor
     * @param context The application context
     */
    public MaterialDao(Context context) {
        try {
            helper = DatabaseHelper.getInstance(context);
            dao = helper.getMaterialDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO CRUD

    /**
     * Add one material record to database
     * @param material The Material object info
     */
    public void addMaterial(Material material) {
        if (dao != null)
            try {
                //dao.create(material);
                dao.createOrUpdate(material);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * Delete target material
     * @param material The material to be deleted
     * @return
     *  The deleted record count
     */
    public int deleteMaterial(Material material) {
        if (dao != null)  {
            try {
                return dao.delete(material);
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
    public List<Material> queryForAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Get all materials which belong to target play
     * @param play The owner of the materials
     * @return The all materials belong to the play
     */
    /*public List<Material> getMaterials(Program play) {
        if (dao != null)  {
            try {
                return dao.queryForEq("play_id", play.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/

    /**
     * Get all materials which have the same md5 property
     * @param md5 The Materials md5
     * @return The list of materials have the same md5
     */
    public List<Material> getMaterials(String md5) {
        if (dao != null) {
            try {
                return dao.queryForEq(Material.MD5_FIELD, md5);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Query all materials which match the given guid
     * @param //guid The guid column of material table
     * @return
     *  material list which match the given guid
     */
    public List<Material> queryByPlayId(String play_id) {
        if (dao != null) {
            try {
                return dao.queryForEq(Material.PROGRAM_ID_FIELD, play_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean update(Material m) {
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
