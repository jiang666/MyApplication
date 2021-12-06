package com.iflytek.demo.db;

import android.content.Context;

import com.iflytek.demo.DatabaseHelper;
import com.iflytek.demo.entity.Play;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The utility to operate playlist table
 */
public class PlayDao {

    private DatabaseHelper helper;
    private Dao<Play, Integer> playDao;

    /**
     * The constructor
     * @param context The application context
     */
    public PlayDao(Context context) {
        try {
            helper = DatabaseHelper.getInstance(context);
            playDao = helper.getPlayDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a new play item to playlist table
     * @param play The Play object
     */
    public synchronized void addPlay(Play play) {
        try {
            if (playDao != null)
                playDao.create(play);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Delete target play
     * @param play The play object to be deleted
     * @return
     *  The count play record deleted
     */
    public int deletePlay(Play play) {
        if (playDao != null) {
            try {
                return playDao.delete(play);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Query play from db by path field
     * @param path The play path field
     * @return
     *  List<Play>: All Play object compare the query condition
     */
    public List<Play> queryByName(String name) {
        try {
            return playDao.queryForEq(Play.NAME_FIELD, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Query all play from database
     * @return
     *  Play list
     */
    public List<Play> queryForAll() {
        try {
            return playDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Update target play field
     * @param play The new Play object
     * @return
     *  true: Update successful
     *  false: Update failed
     */
    public boolean update(Play play) {
        try {
            return playDao.update(play) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
