package com.iflytek.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.iflytek.demo.db.entity.Face;
import com.iflytek.demo.db.entity.Install;
import com.iflytek.demo.db.entity.Material;
import com.iflytek.demo.db.entity.Program;
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

    private Dao<Face, Integer> faceDao;
    private Dao<Program, Integer> playDao;
    private Dao<Material, Integer> materialDao;
    private Dao<Install, Integer> installDao;

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
            TableUtils.createTable(connectionSource, Program.class);
            // Create materials table
            TableUtils.createTable(connectionSource, Material.class);
            // Create install table
            TableUtils.createTable(connectionSource, Install.class);
            // Create face table
            TableUtils.createTable(connectionSource, Face.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // Drop playlist table
            TableUtils.dropTable(connectionSource, Program.class, true);
            // Drop materials table
            TableUtils.dropTable(connectionSource, Material.class, true);
            // Drop install table
            TableUtils.dropTable(connectionSource, Install.class, true);
            // Drop face table
            TableUtils.dropTable(connectionSource, Face.class, true);
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
        installDao = null;
        faceDao = null;
    }

    /**
     * Get plays table Dao object
     * @return The Dao for plays table
     * @throws SQLException
     */
    public Dao<Program, Integer> getPlayDao() throws SQLException {
        if (playDao == null) {
            playDao = getDao(Program.class);
        }
        return playDao;
    }

    /**
     * Get face table Dao object
     * @return The Dao for face table
     * @throws SQLException
     */
    public Dao<Face, Integer> getFaceDao() throws SQLException {
        if (faceDao == null) {
            faceDao = getDao(Face.class);
        }
        return faceDao;
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

    /**
     * Get materials table Dao object
     * @return The Dao for install table
     * @throws SQLException
     */
    public Dao<Install, Integer> getInstallDao() throws SQLException {
        if (installDao == null) {
            installDao = getDao(Install.class);
        }
        return installDao;
    }
}
