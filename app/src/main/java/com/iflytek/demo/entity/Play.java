package com.iflytek.demo.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Table: playlist
 * ------------------------------------------------------------------
 *    id    |    name    |    path    |    date    |     state    |
 * -------------------------------------------------------------------
 *   int    |varchar(255)|varchar(255)|   date     |    0|1|2     |
 * --------------------------------------------------------------------
 */
@DatabaseTable(tableName = "playlist")
public class Play {

    // Download state constants
    public static final int PLAY_DOWNLOADING_STATE  = 0;
    public static final int PLAY_DOWNLOADED_STATE   = 1;
    public static final int PLAY_UPDATING_STATE     = 2;

    public static final String NAME_FIELD       = "name";
    public static final String PATH_FIELD       = "path";
    public static final String DATE_FIELD       = "date";
    public static final String STATE_FIELD      = "state";



    /**
     * The unique id for a play in playlist table
     */
    @DatabaseField(generatedId = true)
    private int id;
    /**
     * The play name
     */
    @DatabaseField(columnName = "name")
    private String name;
    /**
     * The play path relative to remote ftp server root
     */
    @DatabaseField(columnName = "path")
    private String path;
    /**
     * The play start download date
     */
    @DatabaseField(columnName = "version")
    private String version;
    /**
     * The play current download state. One of
     * 0: downloading, first download
     * 1: download or update completed
     * 2: updating, already downloaded, backend modify play, need update.
     */
    @DatabaseField(columnName = "state")
    private int state;

    /**
     * Default constructor
     */
    public Play() {

    }
    public Play(String name, String path, String version, int state) {
        this.name = name;
        this.path = path;
        this.version = version;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
