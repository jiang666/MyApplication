package com.iflytek.demo.db.dao;

import android.content.Context;

import com.iflytek.demo.db.DatabaseHelper;
import com.iflytek.demo.db.entity.Program;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The utility to operate playlist table
 */
public class ProgramDao {

    private DatabaseHelper helper;
    private Dao<Program, Integer> playDao;

    /**
     * The constructor
     * @param context The application context
     */
    public ProgramDao(Context context) {
        try {
            helper = DatabaseHelper.getInstance(context);
            playDao = helper.getPlayDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a new program item to playlist table
     * @param program The Program object
     */
    public synchronized void addPlay(Program program) {
        try {
            if (playDao != null)
                playDao.create(program);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Delete target program
     * @param program The program object to be deleted
     * @return
     *  The count program record deleted
     */
    public int deletePlay(Program program) {
        if (playDao != null) {
            try {
                return playDao.delete(program);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Query play from db by path field
     * @param playid The play path field
     * @return
     *  List<Program>: All Program object compare the query condition
     */
    public List<Program> queryByPlayid(String playid) {
        try {
            return playDao.queryForEq(Program.PROGRAM_FIELD, playid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Query all play from database
     * @return
     *  Program list
     */
    public List<Program> queryForAll() {
        try {
            return playDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Update target program field
     * @param program The new Program object
     * @return
     *  true: Update successful
     *  false: Update failed
     */
    public boolean update(Program program) {
        try {
            return playDao.update(program) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
