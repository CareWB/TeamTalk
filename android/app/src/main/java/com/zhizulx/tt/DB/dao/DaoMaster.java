package com.zhizulx.tt.DB.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.zhizulx.tt.DB.dao.DepartmentDao;
import com.zhizulx.tt.DB.dao.UserDao;
import com.zhizulx.tt.DB.dao.GroupDao;
import com.zhizulx.tt.DB.dao.MessageDao;
import com.zhizulx.tt.DB.dao.SessionDao;
import com.zhizulx.tt.DB.dao.TravelDao;
import com.zhizulx.tt.DB.dao.PlayConfigDao;
import com.zhizulx.tt.DB.dao.SightDao;
import com.zhizulx.tt.DB.dao.HotelDao;
import com.zhizulx.tt.DB.dao.TrafficDao;
import com.zhizulx.tt.DB.dao.DetailDispDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 12): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 12;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        DepartmentDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        GroupDao.createTable(db, ifNotExists);
        MessageDao.createTable(db, ifNotExists);
        SessionDao.createTable(db, ifNotExists);
        TravelDao.createTable(db, ifNotExists);
        PlayConfigDao.createTable(db, ifNotExists);
        SightDao.createTable(db, ifNotExists);
        HotelDao.createTable(db, ifNotExists);
        TrafficDao.createTable(db, ifNotExists);
        DetailDispDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        DepartmentDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        GroupDao.dropTable(db, ifExists);
        MessageDao.dropTable(db, ifExists);
        SessionDao.dropTable(db, ifExists);
        TravelDao.dropTable(db, ifExists);
        PlayConfigDao.dropTable(db, ifExists);
        SightDao.dropTable(db, ifExists);
        HotelDao.dropTable(db, ifExists);
        TrafficDao.dropTable(db, ifExists);
        DetailDispDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(DepartmentDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(GroupDao.class);
        registerDaoClass(MessageDao.class);
        registerDaoClass(SessionDao.class);
        registerDaoClass(TravelDao.class);
        registerDaoClass(PlayConfigDao.class);
        registerDaoClass(SightDao.class);
        registerDaoClass(HotelDao.class);
        registerDaoClass(TrafficDao.class);
        registerDaoClass(DetailDispDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
