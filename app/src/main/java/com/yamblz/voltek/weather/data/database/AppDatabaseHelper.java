package com.yamblz.voltek.weather.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.yamblz.voltek.weather.data.database.models.DaoMaster;
import com.yamblz.voltek.weather.utils.LogUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class AppDatabaseHelper extends DaoMaster.OpenHelper {

    private final Context context;

    private SQLiteDatabase sqliteDatabase;

    private String dbPath;

    private String dbName;

    public AppDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        this.context = context;
        dbName = name;
        dbPath = context.getApplicationInfo().dataDir + "/databases/";
        try {
            createDataBase();
        } catch (Exception ioe) {
            throw new Error("Unable to create database");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /**
     * Open Database for Use
     */
    public void openDatabase() {
        String databasePath = dbPath + dbName;
        sqliteDatabase = SQLiteDatabase.openDatabase(databasePath, null,
                (SQLiteDatabase.OPEN_READWRITE));
    }

    /**
     * Close Database after use
     */
    @Override
    public synchronized void close() {
        if ((sqliteDatabase != null) && sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        super.close();
    }

    /**
     * Get database instance for use
     */
    public SQLiteDatabase getSqliteDatabase() {
        return sqliteDatabase;
    }

    /**
     * Create new database if not present
     */
    private void createDataBase() {
        SQLiteDatabase sqliteDatabase;

        if (!databaseExists()) {
            /* Database does not exists create blank database */
            sqliteDatabase = this.getReadableDatabase();
            sqliteDatabase.close();
            copyDataBase();
        }

        DaoMaster.createAllTables(getWritableDb(), true);
    }

    /**
     * Check Database if it exists
     */
    private boolean databaseExists() {
        SQLiteDatabase sqliteDatabase = null;
        try {
            String databasePath = dbPath + dbName;
            sqliteDatabase = SQLiteDatabase.openDatabase(databasePath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
        return sqliteDatabase != null;
    }

    /**
     * Copy existing database file in system
     */
    private void copyDataBase() {

        int length;
        byte[] buffer = new byte[1024];
        String databasePath = dbPath + dbName;

        try {

            InputStream databaseInputFile = this.context.getAssets().open(dbName);
            OutputStream databaseOutputFile = new FileOutputStream(databasePath);

            while ((length = databaseInputFile.read(buffer)) > 0) {
                databaseOutputFile.write(buffer, 0, length);
                databaseOutputFile.flush();
            }
            databaseInputFile.close();
            databaseOutputFile.close();

        } catch (IOException e) {
            LogUtils.log("error", e);
        }

    }


}
