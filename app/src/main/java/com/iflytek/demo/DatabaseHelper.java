package com.iflytek.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.iflytek.demo.entity.Material;
import com.iflytek.demo.entity.Play;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * The DatabaseHelper for bunnytouch.db
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG     = DatabaseHelper.class.getSimpleName();
    private static final boolean Debug  = true;

    private static final String DATABASE_NAME   = "bunnytouch.db";
    private static final int DATABASE_VERSION   = 1;

    private Dao<Play, Integer> playDao;
    private Dao<Material, Integer> materialDao;

    private static DatabaseHelper databaseHelper;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper(context);
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // Create playlist table
            TableUtils.createTable(connectionSource, Play.class);
            // Create materials table
            TableUtils.createTable(connectionSource, Material.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // Drop playlist table
            TableUtils.dropTable(connectionSource, Play.class, true);
            // Drop materials table
            TableUtils.dropTable(connectionSource, Material.class, true);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        playDao = null;
        materialDao = null;
    }

    /**
     * Get plays table Dao object
     * @return The Dao for plays table
     * @throws SQLException
     */
    public Dao<Play, Integer> getPlayDao() throws SQLException {
        if (playDao == null) {
            playDao = getDao(Play.class);
        }
        return playDao;
    }

    /**
     * Get materials table Dao object
     * @return The Dao for materials table
     * @throws SQLException
     */
    public Dao<Material, Integer> getMaterialDao() throws SQLException {
        if (materialDao == null) {
            materialDao = getDao(Material.class);
        }
        return materialDao;
    }
}
